/**
 * 异步支持
 */


/****
 * 方式1 async await
 */
void checkVersion() async {
  try {
    print("开始请求");
    var future = await testTimeOut();
    print("异步数据来了" + future.substring(0));
  } catch (e) {
    //todo 处理一下
  }
}

Future<String> testTimeOut() {
  var result = new Future<String>.delayed(
      const Duration(seconds: 5), () => "1.0");
  return result;
}


main() {
  checkVersion();
}