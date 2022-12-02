import 'dart:convert';
import 'dart:io';
import 'dart:math';

void main() async {
  final input = await File('../input.txt').readAsString();

  final reduced =
      LineSplitter().convert(input).fold([0], ((previousValue, element) {
    if (element == '') {
      previousValue.add(0);
    } else {
      previousValue.last += int.parse(element);
    }

    return previousValue;
  }));

  reduced.sort((a, b) => b - a);

  print(reduced.reduce(max));
  print(reduced.getRange(0, 3).reduce((value, element) => value + element));
}
