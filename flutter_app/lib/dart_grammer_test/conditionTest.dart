testFor() {
  var message = StringBuffer('Dart is fun');
  for (var i = 0; i < 5; i++) {
    message.write('!');
  }
  var list = [1, 2];

  list.forEach((element) {
    print(element);
  });

  for (var x in list) {
    print(x);
  }

  //针对iterator的条件筛选
  list.where((element) => element > 1).forEach((element) {
    print("where ${element}");
  });
}

main() {
  testFor();
}
