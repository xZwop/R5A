//! Boundary for LineId Generator
Logoot.BOUNDARY = undefined;

/*!
 * \class   Logoot
 * \brief   Logoot algorithm implementation.
 *
 * Logoot algorithm implementation.
 */
function Logoot() {
}

Logoot.clock = 0;

/*!
 * \brief   Generation of a position, logoot algorithm.
 *
 * \param   previousLineId  The previous LineId.
 * \param   nextLineId      The next LineId.
 * \param   N               Number of positions generated.
 * \param   boundary        
 * \param   rep             Unique user replica.
 * \param   clock           Unique user clock at this time.
 * \return  List of N LineId between previous and next LineId.
 */
Logoot.generateLineId = function(previousLineId, nextLineId, N,
    boundary, replica) {
  var prefPrev = 0;
  var prefNext = 0;
  var index = 0;
  var interval = 0;

  // Compute index to ensure between p and q you could put N new LineId.
  while (interval < N) {
    index ++;

    // Compute prefix
    prefPrev = Logoot.prefix(previousLineId, index);
    prefNext = Logoot.prefix(nextLineId, index);

    // Compute interval
    interval = prefNext - prefPrev - 1;
  }

  // Construct Indentifier.
  var step = interval/N;
  step = (boundary) ? Math.min(Math.round(step), boundary) : Math.round(step);
  var r = prefPrev;
  var list = [];

  // -- DEBUG START
  /*
  console.log('prefixPreviousLineId:' + prefPrev);
  console.log('prefixNextLineId:' + prefNext);
  console.log('interval:' + interval);
  console.log('step:' + step);
  //*/
  // -- DEBUG END

  for (var j = 1; j <= N; j++) {
    list.push(Logoot.constructLineId(r + rand(1, step), previousLineId,
          nextLineId, replica));

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
  var min = Math.min(index, lineId.length());
  
  // Get each position.getInt and put it in right \c BASE.
  for (var id = 0; id < min; ++ id) {
    result += str_pad(lineId.getPosition(id).getInt().toString(), DIGIT, '0',
        'STR_PAD_LEFT');
  }

  // If index is bigger than positions available, fill with 0.
  while (min < index) {
    result += str_pad('', DIGIT, '0');
    ++ min;
  }

  return Number(result);
}

/*!
 * \brief   Generate randomly a LineId.
 *
 * \param   r             value to generate LineId.
 * \param   startLineId   LineId from generate r.
 * \param   endLineId     LineId to generate r.
 * \param   replica       User unqiue replica.
 */
Logoot.constructLineId = function(r, startLineId, endLineId, replica) {
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

  // Generate position of lineId.
  for (var i in chunksR) {
    var position;
    var d = Number(chunksR[i]);

    if (i < startLineId.length()
        && d == startLineId.getPosition(i).getInt()) {
      position = new Position(d,
          startLineId.getPosition(i).getReplica(),
          startLineId.getPosition(i).getClock());
    } else if (i < endLineId.length()
        && d == endLineId.getPosition(i).getInt()) {
      position = new Position(d,
          endLineId.getPosition(i).getReplica(),
          endLineId.getPosition(i).getClock());
    } else {
      position = new Position(d, replica, Logoot.clock);
      ++Logoot.clock;
    }

    if(isNaN(position.getInt())) {
      console.error("NaN in apostition.");
    }
    lineId.add(position);
  }

  return lineId;
}

