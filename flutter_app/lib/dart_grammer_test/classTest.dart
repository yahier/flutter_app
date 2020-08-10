/**
 * 这些和java的用法相同：抽象类，接口 静态方法,异常，泛型
 */

class Point {
  num x;
  num y;

  /**不像java ,dart的构造函数不能重载 但命名构造函数是个更好的途径**/
  //Point();

  // 命名构造函数
  Point.origin() {
    x = 0;
    y = 0;
  }

  // 指向主构造函数
  Point.alongXAxis(num x) : this(x, 0);

  Point(num x, num y) {
    // 还有更好的方式来实现下面代码，敬请关注。
    this.x = x;
    this.y = y;
  }
}

/**
 * 通过实现类的 call() 方法， 能够让类像函数一样被调用。step2
 */
class WannabeFunction {
  call(String a, String b, String c) => '$a $b $c!';
}

void main() {
  var point = Point(1, 2);
  Point.alongXAxis(3);
  Point.origin();
  point.x = 4; // Use the setter method for x.
  assert(point.x == 4); // Use the getter method for x.
  assert(point.y == null); // Values default to null.

  /***step2*/
  var wf = new WannabeFunction();
  var out = wf("Hi", "there,", "gang");
  print('$out');
}
