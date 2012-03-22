// NOT for production use.
// http://karlagius.wordpress.com
// TODO: Need to figure out how arguments.caller works and where arguments.callee has gone. Both items would be useful to have.
// TODO: Need to find out how to derive the name of the target function if at all possible.

// Performs a deep copy of an object. This allows us to store the
// original function when we apply an aspect to it.
// ref http://www.irt.org/script/879.htm
function CloneObject(instance) {
	for (i in instance) {
		if (typeof instance[i] == 'object') {
			this[i] = new CloneObject(instance[i]);
		}
		else
			this[i] = instance[i];
	}
}

// Utility method, applies a target and aspect to the implementation specifying the aspect's order.
function Aspect(target, aspect, implementation)
{
	implementation["target"] = CloneObject(target);
	implementation["aspect"] = aspect;
	
	return implementation;
}

// Applies an aspect to the target function, which will be executed after the function has exited successfully.
// The aspect will be provided with the arguments to the function, and the return value.
// For the purpose of this function, a successful exit will be considered to be any return not throwing an exception.
function AspectOnSuccess(target, aspect)
{

	return Aspect(target, aspect, function() {
			var val = target.apply(arguments.caller, arguments);
			aspect(arguments, val);
			return val;
		});

	
}

// Applies an aspect to the target function, which will be run before the target is executed.
// The aspect will be provided with the arguments to a function.
function AspectBefore(target, aspect)
{
	return Aspect(target, aspect, function() {
			aspect(arguments);
			var val = target.apply(target, arguments);
			return val;
		});
}

// Applies an aspect to the target function, which will be run if an exception is raised by the target.
function AspectOnException(target, aspect)
{
	return Aspect(target, aspect, function() {
			var val;

			try
			{
				val = target.apply(arguments.caller, arguments);

			}
			catch(err)
			{
				aspect(arguments, err);
			}

			return val;
		});
}