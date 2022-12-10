type command = Noop | Addx of int

let parse_command line =
  match line with
  | "noop" -> Noop
  | l ->
      let [ _; i ] = String.split_on_char ' ' l in
      Addx (int_of_string i)

let produce_cycle command x =
  match command with Noop -> ([ x ], x) | Addx add -> ([ x; x ], x + add)

let make_cycles (cycles, x) command =
  let new_cycles, new_x = produce_cycle command x in
  (List.append new_cycles cycles, new_x)

let signal_strength cycles cycle_index =
  cycle_index * List.nth cycles (cycle_index - 1)

let day01 =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.trim |> String.split_on_char '\n' |> List.map parse_command
  |> List.fold_left make_cycles ([], 1)
  |> fun (cycles, last_x) ->
  List.rev (last_x :: cycles) |> fun cycles ->
  List.fold_left
    (fun acc cycle_index -> acc + signal_strength cycles cycle_index)
    0
    [ 20; 60; 100; 140; 180; 220 ]
