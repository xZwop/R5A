function str_pad(input, pad_length, pad_string, pad_type) {
    // Returns input string padded on the left or right to specified length
    // with pad_string  
    // 
    // version: 1109.2015
    // discuss at: http://phpjs.org/functions/str_pad
    // +   original by: Kevin van Zonneveld (http://kevin.vanzonneveld.net)
    // + namespaced by: Michael White (http://getsprink.com)
    // +      input by: Marco van Oort
    // +   bugfixed by: Brett Zamir (http://brett-zamir.me)
    // *     example 1: str_pad('Kevin van Zonneveld', 30, '-=',
    // *                    'STR_PAD_LEFT');
    // *     returns 1: '-=-=-=-=-=-Kevin van Zonneveld'
    // *     example 2: str_pad('Kevin van Zonneveld', 30, '-', 'STR_PAD_BOTH');
    // *     returns 2: '------Kevin van Zonneveld-----'
    var half = '',
        pad_to_go;
 
    var str_pad_repeater = function (s, len) {
        var collect = '',
            i;
 
        while (collect.length < len) {
            collect += s;
        }
        collect = collect.substr(0, len);
 
        return collect;
    };
 
    input += '';
    pad_string = pad_string !== undefined ? pad_string : ' ';
 
    if (pad_type != 'STR_PAD_LEFT'
        && pad_type != 'STR_PAD_RIGHT'
        && pad_type != 'STR_PAD_BOTH') {
        pad_type = 'STR_PAD_RIGHT';
    }
    if ((pad_to_go = pad_length - input.length) > 0) {
        if (pad_type == 'STR_PAD_LEFT') {
            input = str_pad_repeater(pad_string, pad_to_go) + input;
        } else if (pad_type == 'STR_PAD_RIGHT') {
            input = input + str_pad_repeater(pad_string, pad_to_go);
        } else if (pad_type == 'STR_PAD_BOTH') {
            half = str_pad_repeater(pad_string, Math.ceil(pad_to_go / 2));
            input = half + input + half;
            input = input.substr(0, pad_length);
        }
    }
 
    return input;
}

function str_split (string, split_length) {
    // Convert a string to an array. If split_length is specified, break the
    // string down into chunks each split_length characters long.  
    // 
    // version: 1109.2015
    // discuss at: http://phpjs.org/functions/str_split
    // +     original by: Martijn Wieringa
    // +     improved by: Brett Zamir (http://brett-zamir.me)
    // +     bugfixed by: Onno Marsman
    // +      revised by: Theriault
    // +        input by: Bjorn Roesbeke (http://www.bjornroesbeke.be/)
    // +      revised by: Rafał Kukawski (http://blog.kukawski.pl/)
    // *       example 1: str_split('Hello Friend', 3);
    // *       returns 1: ['Hel', 'lo ', 'Fri', 'end']
    if (split_length === null) {
        split_length = 1;
    }
    if (string === null || split_length < 1) {
        return false;
    }
    string += '';
    var chunks = [],
        pos = 0,
        len = string.length;
    while (pos < len) {
        chunks.push(string.slice(pos, pos += split_length));
    }
 
    return chunks;
}

/*!
 * \brief   Returns random value between min and max.
 *
 * \param   min Min range for random value.
 * \param   max Max range for random value.
 * \return  Random integer value in range <tt>[min..max]</tt>.
 */
function rand(min, max) {
  return Math.round(Math.random() * (max - min) + min);
}

//! BASE constant for position object (if not existe, BASE is set to MAX_INT).
if (!BASE) {
  // var MAX_INT = Math.pow(2, 53);
  var MAX_INT = Math.pow(2, 7);
  var BASE = MAX_INT;
}

/*!
 * \class   Triplet
 * \brief   Triplet meta data.
 *
 * \param   a First element of triplet tuple.
 * \param   b Second element of triplet tuple.
 * \param   c Third element of triplet tuple.
 */
function Triplet(a, b, c) {

   //! The first tuple element.
  this.first = a;

   //! The second tuple element.
  this.second = b;

   //! The third tuple element.
  this.third = c;
}

/*!
 * \brief   Returns element at specific place.
 *
 * Returns element at specific place. Index would be in range [0..2].
 *
 * \param   i   The index place.
 * \return  The element at place \c i or \c null if \c i not in range [0..2].
 */
Triplet.prototype.get = function(i) {
  var value;

  switch (i) {
    case 0:
      value = this.first;
      break;
    case 1:
      value = this.second;
      break;
    case 2:
      value = this.third;
      break;
    default:
      value = null; 
  }

  return value;
}

/*!
 * \class   Position
 * \extends Triplet
 *
 * \param   int_    Integer in range of <tt>[0..BASE[</tt>.
 * \param   replica Unique replica identifier.
 * \param   clock   \c s timestamps (default 0). 
 */
function Position(int_, replica, clock) {
  if (!clock) {
    var clock = 0;
  }

  Triplet.call(this, int_, replica, clock);
}
Position.prototype = new Triplet;  
Position.prototype.constructor = Position;

Position.prototype.getInt = function() {
  return this.first;
}

Position.prototype.getReplica = function() {
  return this.second;
}

Position.prototype.getClock = function() {
  return this.third;
}

/*!
 * \brief   Returns a string representation of the object.
 *
 * \return  A string representation of the object.
 */
Position.prototype.toString = function() {
  return "("
    + this.getInt() + ":"
    + this.getReplica() + ":"
    + this.getClock() + ")";
}

/*!
 * \brief   Returns minimum position value possible.
 * 
 * \return  New Position that is minimum value accepted.
 */
Position.getMin = function() {
  return new Position(0);
}

/*!
 * \brief   Returns maximum position value possible.
 * 
 * \return  New Position that is maximum value accepted (aka \c BASE - 1).
 */
Position.getMax = function() {
  return new Position(BASE - 1);
}

/*!
 * \class LineId
 * \brief Line identifier.
 *
 * LineId is a no mutable ordered Position list. The last Position of the list
 * is the last position of line and position used to create line. Moreover, each
 * position in LineId which have same  \c BASE.
 */
function LineId() {
  //! Positions container.
  this.positions = [];
}

/*!
 * \brief   Adds new position to current Line Identifier.
 *
 * \param   position    The position to add (needs have same \c BASE as others).
 */
LineId.prototype.add = function(position) {
 this.positions.push(position);
}

LineId.prototype.get = function(i) {
  return this.positions[i];
}

/*!
 * \brief   Returns length of Line Identifier.
 *
 * \return  Number of Position in Line Identifier.
 */
LineId.prototype.length = function() {
   return this.positions.length;
}

/*!
 * \brief   Returns a string representation of the object.
 *
 * \return  A string representation of the object.
 */
LineId.prototype.toString = function() {
  var str;

  str = "[";
  for (var key in this.positions) {
    str += this.positions[key].toString();
  }
  str += "]";

  return str;
}

/*!
 * \brief   Returns new Line Identifier with min position only.
 *
 * The Line Identifier with min position represente the starter of document.
 *
 * \return  New LineId with Min Position.
 */
LineId.getMinPosition = function() {
  var lineId = new LineId();

  lineId.add(Position.getMin());
  return lineId;
}

/*!
 * \brief   Returns new Line Identifier with max position only.
 *
 * The Line Identifier with max position represente the end of document.
 *
 * \return  New LineId with Max Position.
 */
LineId.getMaxPosition = function() {
  var lineId = new LineId();

  lineId.add(Position.getMax());
  return lineId;
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
LineId.generateLineId = function(previousLineId, nextLineId, N, boundary,
    replica, clock) {
  var index = 0;
  var interval = 0;
  var prefixPreviousLineId = [];
  var prefixNextLineId = [];

  prefixPreviousLineId[0] = {cumval:'', idstrval:''};
  prefixNextLineId[0] = {cumval:'', idstrval:''};


  while (interval < N) {
    index ++;

    var pref;

    // Compute prefix
    pref = previousLineId.prefix(index);
    prefixPreviousLineId[index] = {
      idstrval:pref,
      cumval:prefixPreviousLineId[index-1].cumval + pref
    };
    console.log('prefixPreviousLineId[' + index + ']{idstrval:'
        + prefixPreviousLineId[index].idstrval +', cumval:'
        + prefixPreviousLineId[index].cumval +'}');

    pref = nextLineId.prefix(index);
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

    // Cut strNr on (BASE-1) to get each chunk.
    // -- If strNr isn't cutable on BASE-1, add some 0 from left.
    if((strNr.length % (index * (BASE - 1))) != 0) {
      strNr = str_pad(strNr, strNr.length + ((index * (BASE -1)) 
          - strNr.length % (index * (BASE - 1))), '0', 'STR_PAD_LEFT');
    }
    
    var chunksNr = str_split(strNr, (BASE - 1));
    var lineId = new LineId();
    console.log('chunksNr:' + chunksNr.toString());

    for (var i = 1; i <= index; i++) {
      var position;
      var d = chunksNr[i - 1];

      if (i <= previousLineId.length()
          && prefixPreviousLineId[i].idstrval == d) {
        position = new Position(d, previousLineId.get(i - 1).getReplica(),
            previousLineId.get(i - 1).getClock());
      } else if (i <= nextLineId.length()
          && prefixNextLineId[i].idstrval == d) {
        position = new Position(d, nextLineId.get(i - 1).getReplica(),
            nextLineId.get(i - 1).getClock());
      } else {
        position = new Position(d, replica, clock);
      }

      lineId.add(position);
    }

    list.push(lineId);
    r += step;
  }

  return list;
}

//! \brief    Prefix.
LineId.prototype.prefix = function(index) {
    return str_pad(this.positions[index -1].getInt().toString(), BASE - 1, '0',
        'STR_PAD_LEFT');
}

