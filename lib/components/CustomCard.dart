import 'package:flutter/material.dart';

class CustomCard extends StatefulWidget {
  final Widget child;
  final String title;
  final IconData icon;
  final bool isClickable;
  final String route;

  CustomCard(
      {this.child,
      this.title,
      this.icon,
      this.isClickable = false,
      this.route});

  @override
  _CustomCardState createState() => _CustomCardState();
}

class _CustomCardState extends State<CustomCard> {
  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.fromLTRB(0, 10, 0, 10),
      child: Card(
        elevation: 2,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(15.0),
        ),
        child: Column(
          children: <Widget>[
            ListTile(
              leading: Icon(
                this.widget.icon,
                size: 25,
                color: Color.fromARGB(500, 1, 224, 143),
              ),
              title: Text(
                this.widget.title,
                style: TextStyle(
                  fontSize: 22,
                  color: Color.fromARGB(500, 1, 224, 143),
                ),
              ),
              trailing: this.widget.isClickable
                  ? Icon(
                      Icons.open_in_new,
                      color: Color.fromARGB(500, 1, 224, 143),
                    )
                  : null,
              onTap: this.widget.isClickable
                  ? () {
                      Navigator.pushNamed(context, this.widget.route);
                    }
                  : null,
            ),
            Container(
              decoration: BoxDecoration(
                border: Border.all(
                  width: 0.1,
                  color: Color.fromARGB(500, 1, 224, 143),
                ),
              ),
            ),
            Container(
              padding: EdgeInsets.all(15),
              child: this.widget.child,
            )
          ],
        ),
      ),
    );
  }
}
