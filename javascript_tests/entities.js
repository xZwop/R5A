//! BASE constant for position object (if not existe, BASE is set to MAX_INT).
if(!BASE) {
  var MAX_INT = Math.pow(2, 53);
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
 * \param   i Integer in range of <tt>[0..BASE[</tt>.
 * \param   s Unique replica identifier.
 * \param   c \c s timestamps. 
 */
function Position(i, s, c) {
  Triplet.call(this, i, s, c);
}
Position.prototype = new Triplet;  
Position.prototype.constructor = Position;

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

/*!
 * \brief   Returns length of Line Identifier.
 *
 * \return  Number of Position in Line Identifier.
 */
LineId.prototype.length = function() {
   return this.positions.length;
}

/*!
 * \brief   Returns new Line Identifier with min position only.
 *
 * The Line Identifier with min position represente the starter of document.
 *
 * \return  New LineId with Min Position.
 */
LineId.getWithMinPosition = function() {
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
LineId.getWithMaxPosition = function() {
  var lineId = new LineId();

  lineId.add(Position.getMax());
  return lineId;
}


