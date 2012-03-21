//! BASE constant for position object (if not existe, BASE is set to MAX_INT).
if (!BASE) {
	var MAX_INT = Math.pow(2, 32);
	var BASE = MAX_INT;
}

// ! Number of digit in BASE.
var DIGIT = Number(BASE.toString().length - 1);

/*
 * ! \class Triplet \brief Triplet meta data.
 * 
 * \param a First element of triplet tuple. \param b Second element of triplet
 * tuple. \param c Third element of triplet tuple.
 */
function Triplet(a, b, c) {

	// ! The first tuple element.
	this.first = a;

	// ! The second tuple element.
	this.second = b;

	// ! The third tuple element.
	this.third = c;
}

/*
 * ! \brief Returns element at specific place.
 * 
 * Returns element at specific place. Index would be in range [0..2].
 * 
 * \param i The index place. \return The element at place \c i or \c null if \c
 * i not in range [0..2].
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

/*
 * ! \class Position \extends Triplet
 * 
 * \param int_ Integer in range of <tt>[0..BASE[</tt>. \param replica Unique
 * replica identifier. \param clock \c s timestamps (default 0).
 */
function Position(int_, replica, clock) {
	if (!clock) {
		var clock = 0;
	}

	Triplet.call(this, int_, replica, clock);
}
Position.prototype = new Triplet;
Position.prototype.constructor = Position;

/*
 * ! \brief Returns the Integer in range of <tt>[0..BASE[</tt>.
 * 
 * \return The integer in range of <tt>[0..BASE[</tt>.
 */
Position.prototype.getInt = function() {
	return this.first;
}

/*
 * ! \brief Returns replica identifying the user.
 * 
 * \return The unique identifier.
 */
Position.prototype.getReplica = function() {
	return this.second;
}

/*
 * ! \brief Returns the current value of the clock.
 * 
 * \return The value of clock.
 */
Position.prototype.getClock = function() {
	return this.third;
}

/*
 * ! \brief Compare current position with another.
 * 
 * \param position Position to compare with current. \return Result of
 * comparison. Values is one of (-1, 0, 1).
 */
Position.prototype.compareTo = function(position) {
	if (this.getInt() > position.getInt()) {
		return 1;
	} else if (this.getInt() < position.getInt()) {
		return -1;
	}

	if (this.getReplica > position.getReplica()) {
		return 1;
	} else if (this.getReplica < position.getReplica()) {
		return -1;
	}

	if (this.getClock > position.getClock()) {
		return 1;
	} else if (this.getClock < position.getClock()) {
		return -1;
	}

	return 0;
}

Position.prototype.compareToFake = function(position) {
	if (this.getInt() > position.getInt()) {
		return 1;
	} else if (this.getInt() < position.getInt()) {
		return -1;
	} else {
		return 0;
	}
}

/*
 * ! \brief Returns a string representation of the object.
 * 
 * \return A string representation of the object.
 */
Position.prototype.toString = function() {
	return "(" + this.getInt() + ":" + this.getReplica() + ":"
			+ this.getClock() + ")";
}

/*
 * ! \brief Returns minimum position value possible.
 * 
 * \return New Position that is minimum value accepted.
 */
Position.getMin = function() {
	return new Position(1, 0, 0);
}

/*
 * ! \brief Returns maximum position value possible.
 * 
 * \return New Position that is maximum value accepted (aka \c BASE - 1).
 */
Position.getMax = function() {
	return new Position(BASE - 1, 0, 0);
}

/*
 * ! \class LineId \brief Line identifier.
 * 
 * LineId is a no mutable ordered Position list. The last Position of the list
 * is the last position of line and position used to create line. Moreover, each
 * position in LineId which have same \c BASE.
 */
function LineId() {
	// ! Positions container.
	this.positions = [];
}

/*
 * ! \brief Adds new position to current Line Identifier.
 * 
 * \param position The position to add (needs have same \c BASE as others).
 */
LineId.prototype.add = function(position) {
	this.positions.push(position);
}

/*
 * ! \brief Returns position at specified index.
 * 
 * \param i Index of position to return. \return The position object at the
 * specified index (or undefined).
 */
LineId.prototype.getPosition = function(i) {
	return this.positions[i];
}

/*
 * ! \brief Returns length of Line Identifier.
 * 
 * \return Number of Position in Line Identifier.
 */
LineId.prototype.length = function() {
	return this.positions.length;
}

/*
 * \brief Compare a LineId with the current.
 * 
 * \param lineId The LineId to compare with current. \return Result of
 * comparison. Values is one of (-1, 0, 1).
 */
LineId.prototype.compareTo = function(lineId) {
	if (this.length() > 0 && lineId.length() > 0) {
		for ( var i = 0; i < Math.min(this.length(), lineId.length()) -1; ++i) {
			var comparison = this.getPosition(i).compareToFake(lineId.getPosition(i));
			if (comparison != 0) {
				return comparison;
			}
		}
		return this.getPosition(Math.min(this.length(), lineId.length())-1).compareTo(lineId.getPosition(Math.min(this.length(), lineId.length())-1));
	}
	return 0;
}

/*
 * ! \brief Returns a string representation of the object.
 * 
 * \return A string representation of the object.
 */
LineId.prototype.toString = function() {
	var str;

	str = "[";
	for ( var key in this.positions) {
		str += this.positions[key].toString();
	}
	str += "]";

	return str;
}

/*
 * ! \brief Returns a JSON representation of the object.
 * 
 * \return A JSON from object datas.
 */
LineId.prototype.serialize = function() {
	return JSON.stringify(this);
}

/*
 * ! \brief Unserialize a JSON LineId.
 * 
 * \param json JSON datas of LineId. \return A new LineId from JSON datas.
 */
LineId.unserialize = function(json) {
	var data = JSON.parse(json);
	var lineId = new LineId();

	for (i in data.positions) {
		var int_ = data.positions[i].first;
		var replica = data.positions[i].second;
		var clock = data.positions[i].third;

		lineId.add(new Position(int_, replica, clock));
	}

	return lineId;
}

/*
 * ! \brief Returns new Line Identifier with min position only.
 * 
 * The Line Identifier with min position represente the starter of document.
 * 
 * \return New LineId with Min Position.
 */
LineId.getDocumentStarter = function() {
	var lineId = new LineId();

	lineId.add(Position.getMin());
	return lineId;
}

/*
 * ! \brief Returns new Line Identifier with max position only.
 * 
 * The Line Identifier with max position represente the end of document.
 * 
 * \return New LineId with Max Position.
 */
LineId.getDocumentFinisher = function() {
	var lineId = new LineId();

	lineId.add(Position.getMax());
	return lineId;
}
