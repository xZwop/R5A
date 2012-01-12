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
  var prefixPreviousLineId = [];
  var prefixNextLineId = [];

  prefixPreviousLineId[0] = {cumval:'', idstrval:''};
  prefixNextLineId[0] = {cumval:'', idstrval:''};

  // Compute index to ensure between p and q you could put N new LineId.
  while (interval < N) {
    index ++;

    var pref;

    // Compute prefix
    pref = Logoot.prefix(previousLineId, index);
    prefixPreviousLineId[index] = {
      idstrval:pref,
      cumval:prefixPreviousLineId[index-1].cumval + pref
    };
    console.log('prefixPreviousLineId[' + index + ']{idstrval:'
        + prefixPreviousLineId[index].idstrval +', cumval:'
        + prefixPreviousLineId[index].cumval +'}');

    pref = Logoot.prefix(nextLineId, index);
    prefixNextLineId[index] = {
      idstrval:pref,
      cumval:prefixNextLineId[index-1].cumval + pref
    };
    console.log('prefixNextLineId[' + index + ']{idstrval:'
        + prefixNextLineId[index].idstrval +', cumval:'
        + prefixNextLineId[index].cumval +'}');

    // Compute interval
    // Caution -- NEED TO SPECIFIC BASE IN parseInt Function.
    var prefNext = parseInt(prefixNextLineId[index].cumval, 10);
    var prefPrev = parseInt(prefixPreviousLineId[index].cumval, 10);

    interval = prefNext - prefPrev - 1;

    console.log('prefNext:' + prefNext);
    console.log('prefPrev:' + prefPrev);
    console.log('interval:' + interval);
  }

  // Construct Indentifier
  // TODO: integrated boundary : step = Math.min(interval/N, boundary);
  // FIXME: May sur to round step
  var step = Math.round(interval/N);
  var r = parseInt(prefixPreviousLineId[index].cumval, 10);
  var list = [];

  for (var j = 1; j <= N; j++) {
    var nr = r + rand(1, step);
    var strNr = nr.toString();

    console.log('nr:' + nr);
    console.log('strNr:' + strNr);

    // Cut strNr on (DIGIT) to get each chunk.
    // -- If strNr isn't cutable on BASE-1, add some 0 from left.
    if((strNr.length % (index * (DIGIT))) != 0) {
      strNr = str_pad(strNr, strNr.length + ((index * (DIGIT)) 
          - strNr.length % (index * (DIGIT))), '0', 'STR_PAD_LEFT');
    }
    
    var chunksNr = str_split(strNr, (DIGIT));
    var lineId = new LineId();
    console.log('chunksNr:' + chunksNr.toString());

    for (var i = 1; i <= index; i++) {
      var position;
      var d = chunksNr[i - 1];

      if (i <= previousLineId.length()
          && prefixPreviousLineId[i].idstrval == d) {
        position = new Position(d,
            previousLineId.getPosition(i - 1).getReplica(),
            previousLineId.getPosition(i - 1).getClock());
      } else if (i <= nextLineId.length()
          && prefixNextLineId[i].idstrval == d) {
        position = new Position(d,
            nextLineId.getPosition(i - 1).getReplica(),
            nextLineId.getPosition(i - 1).getClock());
      } else {
        position = new Position(d, replica, clock++);
      }

      lineId.add(position);
    }

    list.push(lineId);
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

    return str_pad(lineId.getPosition(index -1).getInt().toString(), DIGIT, '0',
        'STR_PAD_LEFT');
}

