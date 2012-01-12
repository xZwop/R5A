/*!
 * \class   Logoot
 * \brief   Logoot algorithm implementation.
 */
function Logoot() {
}

/*!
 * \brief   Generation of a position, logoot algorithm.
 *
 * \param   previousLineId  The previous LineId.
 * \param   nextLineId      The next LineId.
 * \param   N               Number of positions generated.
 * \param   boundary        
 * \param   rep             Unique user replica.
 * \param   clock           Unique user clock at this time.
 * \return  LineId between previous and next LineId.
 */
Logoot.generateLineId = function(previousLineId, nextLineId, N, boundary,
    replica, clock) {
  var index = 0;
  var interval = 0;

  // Compute index to ensure between p and q you could put N new LineId.
  while (interval < N) {
    index ++;

    // Compute prefix
    // Now pref is cumval and idstrval is the last chunck of cumval split
    // on size(DIGIT).
    // Caution -- NEED TO SPECIFIC BASE IN parseInt Function.
    var prefPrev = Logoot.prefix(previousLineId, index);
    var prefNext = Logoot.prefix(nextLineId, index);

    // Compute interval
    interval = prefNext - prefPrev - 1;

    console.log('prefixPreviousLineId:' + prefPrev);
    console.log('prefixNextLineId:' + prefNext);
    console.log('interval:' + interval);
  }

  // Construct Indentifier.
  //! \todo: integrated boundary : step = Math.min(interval/N, boundary);
  //! \fixme: Ensure to round step
  var step = Math.round(interval/N);
  var r = Logoot.prefix(previousLineId, index);
  var list = [];

  for (var j = 1; j <= N; j++) {
    list.push(Logoot.constructLineId(r + rand(1, step), previousLineId,
          nextLineId, replica, clock));

    r += step;
  }

  return list;
}

/*!
 * \brief   Compute the prefix from a LineId.
 *
 * Returns new number in same base as lineId position integer. It returns
 * \c index first position.getInt from lineId. Each position.getInt is
 * put in same base as \BASE.
 *
 * \param   lineId  The line id to compute prefix.
 * \param   index   Index first position.getInt of lineId.
 * \return  Prefix of lineId.
 */
Logoot.prefix = function(lineId, index) {
  var result = '';
  var max = (index <= lineId.length()) ? index : lineId.length();
  
  // Get each position.getInt and put it in right \c BASE.
  for (var id = 0; id < max; ++ id) {
    result += str_pad(lineId.getPosition(id).getInt().toString(), DIGIT, '0',
        'STR_PAD_LEFT');
  }

  // If index is bigger than positions available, fill with 0.
  while (max < index) {
    result += str_pad('', DIGIT, '0');
    ++ max;
  }

  return parseInt(result, 10);
}

/*!
 * \brief   Generate randomly a LineId.
 *
 * \param   r             value to generate LineId.
 * \param   startLineId   LineId from generate r.
 * \param   endLineId     LineId to generate r.
 * \param   replica       User unqiue replica.
 * \param   clock         User clock (at this time).
 */
Logoot.constructLineId = function(r, startLineId, endLineId, replica, clock) {
  var strR = r.toString();

  // Cut strR on (DIGIT) to get each chunk. if strR isn't cutable on DIGIT,
  // add needed 0 at left.
  var lastChunkSize = strR.length % DIGIT;
  if (lastChunkSize != 0) {
    var zeroToAdd = str_pad('', DIGIT - lastChunkSize, '0');
    strR = zeroToAdd + strR;
  }

  var chunksR = str_split(strR, DIGIT);
  var lineId = new LineId();
  console.log('chunksR:' + chunksR);

  // Generate position of lineId.
  for (var i in chunksR) {
    var position;
    var d = parseInt(chunksR[i], 10);

    if (i < startLineId.length() && d == startLineId.getPosition(i).getInt()) {
      position = new Position(d,
          startLineId.getPosition(i).getReplica(),
          startLineId.getPosition(i).getClock());
    } else if (i < endLineId.length && d == endLineId.getPosition(i).getInt()) {
      position = new Position(d,
          endLineId.getPosition(i).getReplica(),
          endLineId.getPosition(i).getClock());
    } else {
      position = new Position(d, replica, clock++);
    }

    lineId.add(position);
  }

  return lineId;
}

