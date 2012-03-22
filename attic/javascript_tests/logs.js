//! \brief Log the update of idTable.
var logUpdateIdTable = function() {
  console.log('updateIdTable:' + logoot.idTable);
}

//! \brief Print method prototype.
var logPrototype = function(args, name) {
  var prototype = '';

  prototype += '  #-> ' + name + '(';
  for (var argsId = 0; argsId < args.length; ++ argsId) {
    if (argsId < (args.length - 1)) {
      prototype += args[argsId] + ', ';
    } else {
      prototype += args[argsId];
    }
  }
  prototype += ')';


  console.log(prototype);
}

//! \brief Print method return.
var logReturn = function(result) {
  if (result) {
    console.log('  #-> return = ' + result);
    return result;
  } else {
    console.log('  #-> return;');
  }
}

//! \brief Count method execution time.
var logTimeExecution = function(invocation) {
  var startTimer = (new Date()).getTime();

  var res = invocation.proceed();

  var diff = (new Date()).getTime() - startTimer;

  console.log('Executing ' + invocation.method + ' in ' + diff + 'ms');

  return res;
}

// Log
$.aop.after({target: Logoot, method:'insertInIdTable'}, logUpdateIdTable);
$.aop.after({target: Logoot, method:'deleteInIdTable'}, logUpdateIdTable);
$.aop.before({target: Logoot, method:'constructLineId'}, logPrototype);
$.aop.after({target: Logoot, method:'constructLineId'}, logReturn);
$.aop.before({target: Logoot, method:'generateLineId'}, logPrototype);
$.aop.after({target: Logoot, method:'generateLineId'}, logReturn);
$.aop.around({target: Logoot, method:'send'}, logTimeExecution);

