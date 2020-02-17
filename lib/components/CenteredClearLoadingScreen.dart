import 'package:flutter/material.dart';

class CenteredClearLoadingScreen extends StatefulWidget {
  @override
  _CenteredClearLoadingScreenState createState() =>
      _CenteredClearLoadingScreenState();
}

class _CenteredClearLoadingScreenState
    extends State<CenteredClearLoadingScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Center(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              CircularProgressIndicator(
                strokeWidth: 2,
                valueColor: AlwaysStoppedAnimation<Color>(Colors.green[200]),
              ),
              Padding(
                padding: EdgeInsets.all(50),
                child: Text('Loading...'),
              )
            ],
          ),
        ),
      ),
    );
  }
}
