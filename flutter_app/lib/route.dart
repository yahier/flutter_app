import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class NewRoute extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("new Route"),
        ),
        body:Center(
          child:Text("this is a new route")
        )

    );
  }

}