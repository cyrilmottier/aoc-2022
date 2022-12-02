import 'dart:convert';
import 'dart:io';

void main() async {
  final input = await File('./input.txt').readAsString();

  const MATCH_POINTS = {
    'LOOSE': 0,
    'DRAW': 3,
    'WIN': 6,
  };

  const PICK_POINTS = {
    'X': 1,
    'Y': 2,
    'Z': 3,
  };

  final SCORE_TABLE = {
    'X': {
      'A': PICK_POINTS['X']! + MATCH_POINTS['DRAW']!,
      'B': PICK_POINTS['X']! + MATCH_POINTS['LOOSE']!,
      'C': PICK_POINTS['X']! + MATCH_POINTS['WIN']!,
    },
    'Y': {
      'A': PICK_POINTS['Y']! + MATCH_POINTS['WIN']!,
      'B': PICK_POINTS['Y']! + MATCH_POINTS['DRAW']!,
      'C': PICK_POINTS['Y']! + MATCH_POINTS['LOOSE']!,
    },
    'Z': {
      'A': PICK_POINTS['Z']! + MATCH_POINTS['LOOSE']!,
      'B': PICK_POINTS['Z']! + MATCH_POINTS['WIN']!,
      'C': PICK_POINTS['Z']! + MATCH_POINTS['DRAW']!,
    },
  };

  final PICK_MATCH_TABLE = {
    'X': {
      'A': "Z",
      'B': "X",
      'C': "Y",
    },
    'Y': {
      'A': "X",
      'B': "Y",
      'C': "Z",
    },
    'Z': {
      'A': "Y",
      'B': "Z",
      'C': "X",
    },
  };

  final reduced = LineSplitter().convert(input).fold(
    0,
    (previousValue, element) {
      final picks = element.split(' ');
      return previousValue +
          SCORE_TABLE[PICK_MATCH_TABLE[picks[1]]![picks[0]]]![picks[0]]!;
    },
  );

  print(reduced);
}
