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

/*!
 * \brief   Binary Search in Array (Monkey typing) on array.
 * 
 * Caution if they have no value equals to \c value in the array, this
 * implemetation will return the first next uper value 
 * Recursive implementation of binary search:
 * http://en.wikipedia.org/wiki/Binary_search_algorithm
 *
 * \param   value   The value to search.
 * \param   low     The low value (default 0).
 * \param   high    The high value (default this.length).
 * \param   compare Function using as order relation between values in the
 *          array. The function would have same comportement than CompareTo in
 *          java. If the function isn't give this will use "< > ==".
 */
Array.prototype.binarySearch(value, low, high, compare) {
  var low = low || 0;
  var high = high || this.length;

  if (high < low) {
    return value[low];
  }

  var mid = parseInt((low + high) / 2);

  var midHigher =
    (compare) ? (compare(this[mid], value) > 0) : (this[mid] > value);
  var midLower =
    (compare) ? (compare(this[mid], value) < 0) : (this[mid] < value);

  if (midHigher) {
    return this.binarySearch(value, low, mid - 1);
  } else if (midLower) {
    return this.binarySearch(value, mid + 1, high);
  } else {
    return mid;
  }
}

