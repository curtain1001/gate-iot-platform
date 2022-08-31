function isEmpty(value) {
  return value == undefined || value === null || value === ''
      || (value.trim && value.trim().length < 1)
}

function lookup(obj, jpath) {
  var _jpath = jpath.indexOf("/") == 0 ? jpath.substring(1, jpath.length)
      : jpath;
  var idx = _jpath.indexOf("/");
  if (idx > 0) {
    var field = _jpath.substring(0, idx);
    return lookup(obj[field], _jpath.substring((idx + 1), _jpath.length));
  } else {
    return obj[_jpath];
  }
}

function lookParent(obj, jpath) {
  var idx = jpath.lastIndexOf("/");
  if (idx > -1) {
    var field = jpath.substring(0, idx);
    return lookup(obj, field);
  } else {
    return undefined;
  }
}

function rownum(idx) {
  return (parseInt(idx) + 1) + "";
}

function isNumber(str) {
  var re = /^[0-9]*$/;
  if (!re.test(str)) {
    return false;
  }
  return true;
}

// 是否是整数
function isInteger(str) {
  return Math.floor(str) === str;
}