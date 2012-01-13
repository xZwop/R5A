/*!
 * \class   Logoot
 * \brief   Logoot algorithm implementation.
 *
 * \param   
 * \param   insertDoc Function call to insert new content. Function takes
 *          parameters:
 *            \li position => Position where insert content.
 *            \li content => Content to insert.
 * \param   deleteDoc Function call to delete content. Function takes
 *          parameters:
 *           \li position => Position where content is deleted.
 * \param   process   Function describe how to launch logoot. This function
 *          gets content on event, determine an operation 
 */
function Logoot(insertDoc, deleteDoc) {
  this.idTable = [];
  this.idTable.push(LineId.getMinPosition());
  this.idTable.push(LineId.getMaxPosition());

  this.insertDoc = insertDoc;
  this.deleteDoc = deleteDoc;

  // CallbackFunction executing alogorithme (callback call at each event
  // captured):
  //   * One callback before text change.
  //   * One callback after text change.

  // From before: Get the content.

  // From after: making patch, call the Send.
  // Send is the deliver without call of insert and deleteDoc. At end send use
  // ShareComponent to send patch.

  // Callback at each push event. This call the receive.
}

/*!
 * \brief   Deliver modification to the user interface.
 *
 * \param   patch Operations Container.
 */
Logoot.prototype.receive = function(patch) {
  for (opId in patch) {
    var operation = patch[opId];
    var lineId = operation.getLineId();

    switch (operation.getType()) {
      case Operation.INSERT:
        var content = operation.getContent();
        var position = this.binarySearch(lineId);

        this.insertInIdTable(position, lineId);
        this.insertDoc(position, content);
        break;
      case Operation.DELETE:
        var position = this.binarySearch(lineId);

        if (this.idTable[position] == lineId) {
          this.deleteInIdTable(position);
          this.deleteDoc(position);
        }
        break;
      default:
    }
  }
}

/*!
 * \brief  Do a binary search in a the idTable.
 *
 * returns the position of lineId or the first next upper value.
 *
 * \param   lineId  The LineId search in table.
 * \return  The position of \c lineId or the first greater.
 */
Logoot.prototype.binarySearch(lineId) {
  return this.idTable.binarySearch(lineId, 0, this.idTable.length,
      function(lineId1, lineId2) {
        return lineId1.compareTo(lineId2);
      });
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
  //! \todo: integrated boundary : step = Math.min(interval/N, boundary);
  //! \fixme: Ensure to round step
  var step = Math.round(interval/N);
  var r = Logoot.prefix(previousLineId, index);
  var list = [];

  console.log('prefixPreviousLineId:' + prefPrev);
  console.log('prefixNextLineId:' + prefNext);
  console.log('interval:' + interval);
  console.log('step:' + step);

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
    var d = Number(chunksR[i]);

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

