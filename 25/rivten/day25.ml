type snafu = Zero | One | Two | Equal | Minus

let snafu_parse s =
  let rec aux s i =
    if String.length s == i then []
    else
      match s.[i] with
      | '0' -> Zero :: aux s (i + 1)
      | '1' -> One :: aux s (i + 1)
      | '2' -> Two :: aux s (i + 1)
      | '=' -> Equal :: aux s (i + 1)
      | '-' -> Minus :: aux s (i + 1)
  in
  aux s 0 |> List.rev

let rec sum_single a b =
  match (a, b) with
  | Zero, x -> [ x ]
  | One, One -> [ Two ]
  | One, Two -> [ Equal; One ]
  | One, Equal -> [ Minus ]
  | One, Minus -> [ Zero ]
  | Two, Two -> [ Minus; One ]
  | Two, Equal -> [ Zero ]
  | Two, Minus -> [ One ]
  | Equal, Equal -> [ One; Minus ]
  | Equal, Minus -> [ Two; Minus ]
  | Minus, Minus -> [ Equal ]
  | _ -> sum_single b a

let rec sum a b =
  match (a, b) with
  | [], [] -> []
  | a, [] -> a
  | [], b -> b
  | ha :: ra, hb :: rb -> (
      let s = sum_single ha hb in
      match s with
      | [ x ] -> x :: sum ra rb
      | [ x; r ] -> x :: sum [ r ] (sum ra rb))

let snafu_char x =
  match x with
  | Zero -> '0'
  | One -> '1'
  | Two -> '2'
  | Equal -> '='
  | Minus -> '-'

let input =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.trim |> String.split_on_char '\n' |> List.map snafu_parse

let part1 =
  List.fold_left sum [ Zero ] input
  |> List.rev |> List.map snafu_char |> List.to_seq |> String.of_seq
