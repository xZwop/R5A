//==============================================================================
// initialisation
//==============================================================================

var EDITABLE_ID = "logoot";
var BEGIN_LINE_ID = LineId.getDocumentStarter().serialize();
var END_LINE_ID = LineId.getDocumentFinisher().serialize();
var CHARACTER_CLASS = "logoot_character";
var LIMIT_CLASS = "logoot_limit";

// generated in makeLogootEditor()
var identifier = undefined;
var logootPusher = undefined;

function makeLogootEditor(divID) {
  var edit = document.getElementById(divID);

  edit.contentEditable = true;
  edit.addEventListener("keypress", insertion, false);
  edit.addEventListener("paste", paste, false);
  edit.addEventListener("keydown", deletion, false);
  edit.innerHTML = "<span class='"
                   + LIMIT_CLASS
                   + "' id='"
                   + BEGIN_LINE_ID
                   + "'></span><span class='"
                   + LIMIT_CLASS
                   + "' id='"
                   + END_LINE_ID
                   + "'></span>";
  //identifier = ???;
  //logootPusher = new EventSource();

  //logootPusher.onmessage = onReceive;
}

//==============================================================================
// events
//==============================================================================

function onReceive(event) {
  // TODO
}

function insertion(event) {
  var edit = document.getElementById(EDITABLE_ID);
  var selection = window.getSelection();
  var range = selection.getRangeAt(0);
  var next = selection.anchorNode.parentNode.nextSibling;
  var span = document.createElement("span");
  var data = String.fromCharCode(event.keyCode);

  // space
  if(event.keyCode==32) {
    data = "&nbsp;";
  }

  // be sure that the next node is between the begin and the end span
  if(selection.baseOffset==0
     || (selection.baseOffset==1 && selection.anchorNode.id == "logoot")) {
    next=document.getElementById(BEGIN_LINE_ID).nextSibling;
  } else if(next == document.getElementById(BEGIN_LINE_ID)) {
    next = next.nextSibling;  
  } else if(next == null) {
    next = document.getElementById(END_LINE_ID);
  }

  var previousLineIdentifier = next.previousSibling.id;
  var nextLineIdentifier = next.id;

  // the previous or next span could be a cursor so the lineIdentifiers must be
  // changed
  while(document.getElementById(previousLineIdentifier).className != CHARACTER_CLASS
        && document.getElementById(previousLineIdentifier).className != LIMIT_CLASS) {
    previousLineIdentifier = document.getElementById(previousLineIdentifier).previousSibling.id;
  }
  while(document.getElementById(nextLineIdentifier).className != CHARACTER_CLASS
        && document.getElementById(nextLineIdentifier).className != LIMIT_CLASS) {
    nextLineIdentifier = document.getElementById(nextLineIdentifier).nextSibling.id;
  }

  // set the new span
  span.innerHTML = data;
  span.className = CHARACTER_CLASS;
  span.id = Logoot.generateLineId(LineId.unserialize(previousLineIdentifier),
                                  LineId.unserialize(nextLineIdentifier),
                                  1,
                                  10,
                                  identifier)[0].serialize();

  // insert the added character
  edit.insertBefore(span, next);

  // move the caret to the end of the inserted character
  range.selectNode(span);
  range.collapse(false);
  selection.removeAllRanges();
  selection.addRange(range);

  // TODO notify other clients
  // send(???);

  // cancel the event, then the character is not added twice
  event.returnValue = false;
}

function paste(event) {
  alert("Paste not supported.");
  event.returnValue = false;
}

function deletion(event) {
  switch(event.keyCode) {
    // <-
    case 8:
      var edit = document.getElementById(EDITABLE_ID);
      var selection = window.getSelection();
      var range = document.createRange();
      var span = selection.anchorNode.parentNode;

      // TODO notify other clients
      // send(???);

      if(span.id && span.className != LIMIT_CLASS) {
        // move the caret to the end of the previous character
        range.selectNode(span.previousSibling);
        range.collapse(false);
        selection.removeAllRanges();
        selection.addRange(range);

        // delete the character
        edit.removeChild(span);
      }

      // cancel the event, then the character is not added twice
      event.returnValue = false;
    break;

    // del
    case 46:
      var edit = document.getElementById(EDITABLE_ID);
      var selection = window.getSelection();
      var span = selection.anchorNode.parentNode;
      var next = span.nextSibling;

      // space
      if(selection.baseOffset==0
         || (selection.baseOffset==1 && selection.anchorNode.id == "logoot")) {
        next=document.getElementById(BEGIN_LINE_ID).nextSibling;
      }

      // TODO notify other clients
      // send(???);

      // delete the character
      if(next && next.className != LIMIT_CLASS) {
        edit.removeChild(next);
      }

      // cancel the event, then the character is not added twice
      event.returnValue = false;
    break;
  }
}

//==============================================================================
// utilities
//==============================================================================

function foreignInsertion(repID, data, newLineIdentifier,
                          previousLineIdentifier) {
  // do not process its own insertions
  if(repID != identifier) {
    var edit = document.getElementById(EDITABLE_ID);
    var selection = window.getSelection();
    var range = selection.getRangeAt(0);
    var next = document.getElementById(previousLineIdentifier).nextSibling;
    var span = document.createElement("span");

    // set the new span
    span.innerHTML = data;
    span.id = newLineIdentifier;
    span.style = undefined;

    // insert the added character
    edit.insertBefore(span, next);
  }
}

function foreignDeletion(repID, lineIdentifier) {
  // do not process its own insertions
  if(repID != identifier) {
    var edit = document.getElementById(EDITABLE_ID);
    var span = document.getElementById(lineIdentifier);

    // delete the character
    edit.removeChild(span);
  }
}

function addCaretForRepID(repID, previousLineIdentifier) {
  // do not (re)display its own caret
  if(repID != identifier) {
    var edit = document.getElementById(EDITABLE_ID);
    var next = document.getElementById(previousLineIdentifier).nextSibling;
    var span = document.createElement("span");

    // delete the old caret (if it exists)
    removeCaretForRepID(repID);

    // set the span to be a special caret
    span.className = CARET_CLASS;
    span.id = repID;
    span.hidden = false;
    span.innerHTML = "<font size=\"5\" face=\"arial\" style=\"color: "
                     + caretColorForRepID(repID)
                     + "\">|</font>";

    // insert the caret
    edit.insertBefore(span, next);
  }
}

function removeCaretForRepID(repID) {
  // do not (re)display its own caret
  if(repID != identifier) {
    var edit = document.getElementById(EDITABLE_ID);
    var carets = document.getElementsByClassName(CARET_CLASS);

    for(var i = 0; i < carets.length; ++i) {
      if(carets[i].id = repID) {
        edit.removeChild(carets[i]);
      }
    }
  }
}

function caretColorForRepID(repID) {
  return "red"; // TODO generated with the repID
}

function closestSpan(newLineIdentifier) {
  var edit = document.getElementById(EDITABLE_ID);
  var span = edit.firstChild;
  var spanLineID = LineId.unserialize(span.id);
  var newLineID = LineId.unserialize(newLineIdentifier);
  var compareTo = spanLineId.compareTo(newLineID);

  while(compareTo == 1) {
    span = span.nextSibling;
    spanLineID = LineId.unserialize(span.id);
    compareTo = spanLineId.compareTo(newLineID);
  }

  return span;
}

