/*!
 * \class   Logoot
 * \brief   Logoot algorithm implementation.
 *
 * Logoot algorithm implementation. For works this implementation use the
 * diff_match_patch library.
 *
 * \sa      http://code.google.com/p/google-diff-match-patch
 * \param   dmp   Diff_patch_match component.
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
function Logoot(dmp, insertDoc, deleteDoc) {
  var minLineId = LineId.getDocumentStarter();
  var maxLineId = LineId.getDocumentFinisher();

  //! Diff Match Patch Component.
  this.dmp = dmp;

  //! Container of LineId.
  this.idTable = [];
  this.idTable.push(minLineId);
  this.idTable.push(maxLineId);

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
 * \brief   Before send, this method would be call to get the content.
 *
 * \param   content   The content before the text change.
 */
Logoot.prototype.beforeSend = function(content) {
  this.content = content;
}

/*!
 * \brief   Compute the patch and send it to other.
 *
 * Compute the patch. Maintain the id table and send result to other. At
 * the end, this will call the Shared component to send patch.
 *
 * \param   newContent  Content after change.
 */
var DIFF_NOCHANGE = 0;
var DIFF_INSERT = 1;
var DIFF_DELETE = -1;
Logoot.prototype.send = function(newContent) {
  var patch = [];
  var curPos = 0;

  // Compute patch
  var diffs = this.dmp.diff_main(this.content, newContent);
  for (diffId in diffs) {
    var diffType = diffs[diffId][0];
    var diffStr = diffs[diffId][1];

    if (diffType == DIFF_INSERT) {
      var previousLineId = this.idTable[curPos];
      var nextLineId = this.idTable[curPos + 1];

      var lineIds = Logoot.generateLineId(previousLineId, nextLineId,
          diffStr.length, 10, 1, 1);

      for (id in lineIds) {
        var lineId = lineIds[id];
        var content = diffStr[id];

        patch.push(new OperationInsert(lineId, content));
      }

      curPos += diffStr.length;
    } else if (diffType == DIFF_DELETE) {

      for (var charId in diffStr) {
        var lineId = this.idTable[curPos];

        patch.push(new OperationDelete(lineId));
        -- curPos;
      }
    } else if (diffType == DIFF_NOCHANGE) {
      curPos += diffStr.length
    }
  }

  // Integrated patch
  for (opId in patch) {
    var operation = patch[opId];
    var lineId = operation.getLineId();

    switch (operation.getType()) {
      case Operation.INSERT:
        var content = operation.getContent();
        var position = this.binarySearch(lineId);

        this.insertInIdTable(position, lineId);
        break;
      case Operation.DELETE:
        var position = this.binarySearch(lineId);

        if (this.idTable[position] == lineId) {
          this.deleteInIdTable(position);
        }
        break;
      default:
    }
  }

  console.log(this.idTable.toString());
  // From after: making patch, call the Send.
  // Send is the deliver without call of insert and deleteDoc. At end send use
  // ShareComponent to send patch.
}

/*!
 * \brief  Do a binary search in a the idTable.
 *
 * returns the position of lineId or the first next upper value.
 *
 * \param   lineId  The LineId search in table.
 * \return  The position of \c lineId or the first greater.
 */
Logoot.prototype.binarySearch = function(lineId) {
  return binarySearch(this.idTable, lineId, 0, (this.idTable.length - 1),
      function(lineId1, lineId2) {
        return lineId1.compareTo(lineId2);
      });
}

/*!
 * \brief   Insert new LineId in idTable.
 *
 * This will insert new LineId in idTable and move next value to upper
 * index.
 *
 * \param   index   The index where insert LineId.
 * \param   lineId  LineId to insert.
 */
Logoot.prototype.insertInIdTable = function(index, lineId) {
  var newIdTable = [];

  for (var i = 0; i < index; ++ i) {
    newIdTable[i] = this.idTable[i];
  }
  newIdTable[index] = lineId;
  for (var i = index; i < this.idTable.length; ++ i) {
    newIdTable[i + 1] = this.idTable[i];
  }

  this.idTable = newIdTable;
}

/*!
 * \brief   Delete LineId at specific position in idTable.
 *
 * This will delete the LineId at specific position and move
 * all next value at lower index.
 *
 * \param   index   Index where delete LineId.
 */
Logoot.prototype.deleteInIdTable = function(index) {
  var newIdTable = [];

  for (var i = 0; i < index; ++ i) {
    newIdTable[i] = this.idTable[i];
  }
  for (var i = index; i < this.idTable.length; ++ i) {
    newIdTable[i] = this.idTable[i + 1];
  }

  this.idTable = newIdTable;
}

/*!
 * \brief   Returns a string representation of the object.
 *
 * \return  A string representation of the object.
 */
Logoot.prototype.toString = function() {
  return this.idTable.toString();
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
  // -- DEBUG START
  /*
  console.log('chunksR:' + chunksR);
  //*/
  // -- DEBUG END

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
      position = new Position(d, replica, clock++);
    }

    lineId.add(position);
  }

  return lineId;
}

