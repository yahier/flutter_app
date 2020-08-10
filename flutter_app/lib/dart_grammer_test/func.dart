void foo() {} // 顶级函数

class A {
  static void bar() {} // 静态方法
  void baz() {} // 示例方法

/**增加一个带参的构造函数**/

}

/**
 * 返回一个函数
 */
Function makeAdder(num addBy) {
  return (num i) => addBy + i;
}

void test(){
  /***级联运算符 */
//  querySelector('#confirm') // 获取对象。
//    ..text = 'Confirm' // 调用成员变量。
//    ..classes.add('important')
//    ..onClick.listen((e) => window.alert('Confirmed!'));

  /**相当于**/
//  var button = querySelector('#confirm');
//  button.text = 'Confirm';
//  button.classes.add('important');
//  button.onClick.listen((e) => window.alert('Confirmed!'));
}

void main() {
  var x;

  // 比较顶级函数。
  x = foo;
  assert(foo == x);

  // 比较静态方法。
  x = A.bar;
  assert(A.bar == x);

  // 比较实例方法。
  var v = A(); // A的1号实例
  var w = A(); // A的2号实例
  var y = w;
  x = w.baz;

  // 两个闭包引用的同一实例（2号）,
  // 所以它们相等。
  assert(y.baz == x);

  // 两个闭包引用的非同一个实例，
  // 所以它们不相等。
  assert(v.baz != w.baz);
}
