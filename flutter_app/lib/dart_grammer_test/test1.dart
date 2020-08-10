printInteger(int aNumber) {
  print('The number is $aNumber.'); // 打印到控制台。
}

/**定义变量*/
testInitVar() {
  var name = "bingo";
  //name = 1; 编译错误
  name = "bingo2";

  const id = 12;
  //id = 13; //编译错误

  dynamic age = 12;
  age = "haha";

  final hobby = "play ball";
  //hobby = ""; //编译错误

  var s1 = 'String '
      'concatenation'
      " works even over line breaks."; //可以用+，或者省略 自动拼接

  var s2 = '''
You can create
multi-line strings like this one.
''';

  print(s1);
  print(s2);
}

/**
 * set数据
 */
testSet() {
  var list = [1, 2, 3];
  assert(list.length == 3);
  assert(list[1] == 2);
  //set
  var halogens = {'fluorine', 'chlorine', 'bromine', 'iodine', 'astatine'};
  var elements = <String>{};
  elements.add('fluorine');
  elements.addAll(halogens);

  //map
  var gifts = {
    // Key:    Value
    'first': 'partridge',
    'second': 'turtledoves',
    'fifth': 'golden rings'
  };

  var gifts2 = Map();
  gifts['first'] = 'partridge';
  gifts['second'] = 'turtledoves';
  gifts['fifth'] = 'golden rings';

  var nobleGases = Map();
  nobleGases[2] = 'helium';

  var names = <String>['Seth', 'Kathy', 'Lars'];
  var uniqueNames = <String>{'Seth', 'Kathy', 'Lars'};
  var pages = <String, String>{
    'index.html': 'Homepage',
    'robots.txt': 'Hints for web robots',
    'humans.txt': 'We are people, not machines'
  };
}

/**
 * 条件语句
 */
testCondition() {
  var one = int.parse('1');
  var name = "bingo";
  var type = name.runtimeType;

  switch (type) {
    case int:
      print("类型是int");
      break;
    case String:
      print("类型是String");
      break;
  }

  /**判定运算符 is  as is! */
  if (name is String) {
    // Type check
    var length = name.length;
  }
  //(name as String).firstName = 'Bob';

  // 如果name为空时，将变量赋值给name，否则，name的值保持不变。
  name ??= "newValue";

  /***条件语句*/
  var isPublic = true;
  var visibility = isPublic ? 'public' : 'private';

  /***有点像 非空判断*/
  // 如果 p 为 non-null，设置它变量 y 的值为 4。
  // p?.y = 4;类似于非空语句判断
}

/***
 * 参数
 */
void testParas() {
  //将参数放到 [] 中来标记参数是可选的：
  //方法中 再定义方法
  String say(String from, String msg, [String device]) {
    var result = '$from says $msg';
    if (device != null) {
      result = '$result with a $device';
    }
    return result;
  }

  //默认值 方法
  void enableFlags({bool bold = false, bool hidden = false}) {}

  /**一个函数可以作为另一个函数的参数*/
  void printElement(int element) {
    print(element);
  }

  var list = [1, 2, 3];
// 将 printElement 函数作为参数传递。
  list.forEach(printElement);

  /**同样可以将一个函数赋值给一个变量，例如 */
  var loudify = (msg) => '!!! ${msg.toUpperCase()} !!!';
  assert(loudify('hello') == '!!! HELLO !!!');

  /***匿名函数 和java8稍微有点点不同*/
  var list2 = ['apples', 'bananas', 'oranges'];
  list2.forEach((item) {
    print('${list2.indexOf(item)}: $item');
  });
}

void test() {
  bool topLevel = true;

  void main() {
    var insideMain = true;

    void myFunction() {
      var insideFunction = true;

      void nestedFunction() {
        var insideNestedFunction = true;

        assert(topLevel);
        assert(insideMain);
        assert(insideFunction);
        assert(insideNestedFunction);
        print("$topLevel $insideMain $insideFunction $insideNestedFunction");
      }
    }
  }

  main();
  //nestedFunction();编译错误  不能引用
}

/**
 * 返回一个函数
 */
Function makeAdder(num addBy) {
  return (num i) => addBy + i;
}

// 应用从这里开始执行。
main() {
  var number = 41; // 声明并初始化一个变量。
  printInteger(number); // 调用函数。
  test();

  // 创建一个加 2 的函数。
  var add2 = makeAdder(2);
  // 创建一个加 4 的函数。
  var add4 = makeAdder(4);
}
