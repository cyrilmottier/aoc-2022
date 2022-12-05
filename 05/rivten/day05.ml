type move = { start_index : int; end_index : int; move_count : int }

let input =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.split_on_char '\n'

let parse_input input =
  let move_unparsed, start_pos =
    List.partition (String.starts_with ~prefix:"move") input
  in
  List.map
    (fun x ->
      x |> String.split_on_char ' '
      |> fun [ _; move_move_count; _; from; _; to_ ] ->
      {
        move_count = int_of_string move_move_count;
        start_index = int_of_string from;
        end_index = int_of_string to_;
      })
    move_unparsed

let rec make_move piles move =
  match move with
  | { move_count = 0; _ } -> ()
  | { move_count = c; start_index = si; end_index = ei } ->
      let start_stack = piles.(si - 1) and end_stack = piles.(ei - 1) in
      let x = Stack.pop start_stack in
      Stack.push x end_stack;
      make_move piles { move_count = c - 1; start_index = si; end_index = ei }

let piles =
  let tmp = Array.init 9 (fun _ -> Stack.create ()) in
  Stack.push 'H' tmp.(0);
  Stack.push 'C' tmp.(0);
  Stack.push 'R' tmp.(0);
  Stack.push 'B' tmp.(1);
  Stack.push 'J' tmp.(1);
  Stack.push 'H' tmp.(1);
  Stack.push 'L' tmp.(1);
  Stack.push 'S' tmp.(1);
  Stack.push 'F' tmp.(1);
  Stack.push 'R' tmp.(2);
  Stack.push 'M' tmp.(2);
  Stack.push 'D' tmp.(2);
  Stack.push 'H' tmp.(2);
  Stack.push 'J' tmp.(2);
  Stack.push 'T' tmp.(2);
  Stack.push 'Q' tmp.(2);
  Stack.push 'S' tmp.(3);
  Stack.push 'G' tmp.(3);
  Stack.push 'R' tmp.(3);
  Stack.push 'H' tmp.(3);
  Stack.push 'Z' tmp.(3);
  Stack.push 'B' tmp.(3);
  Stack.push 'J' tmp.(3);
  Stack.push 'R' tmp.(4);
  Stack.push 'P' tmp.(4);
  Stack.push 'F' tmp.(4);
  Stack.push 'Z' tmp.(4);
  Stack.push 'T' tmp.(4);
  Stack.push 'D' tmp.(4);
  Stack.push 'C' tmp.(4);
  Stack.push 'B' tmp.(4);
  Stack.push 'T' tmp.(5);
  Stack.push 'H' tmp.(5);
  Stack.push 'C' tmp.(5);
  Stack.push 'G' tmp.(5);
  Stack.push 'S' tmp.(6);
  Stack.push 'N' tmp.(6);
  Stack.push 'V' tmp.(6);
  Stack.push 'Z' tmp.(6);
  Stack.push 'B' tmp.(6);
  Stack.push 'P' tmp.(6);
  Stack.push 'W' tmp.(6);
  Stack.push 'L' tmp.(6);
  Stack.push 'R' tmp.(7);
  Stack.push 'J' tmp.(7);
  Stack.push 'Q' tmp.(7);
  Stack.push 'G' tmp.(7);
  Stack.push 'C' tmp.(7);
  Stack.push 'L' tmp.(8);
  Stack.push 'D' tmp.(8);
  Stack.push 'T' tmp.(8);
  Stack.push 'R' tmp.(8);
  Stack.push 'H' tmp.(8);
  Stack.push 'P' tmp.(8);
  Stack.push 'F' tmp.(8);
  Stack.push 'S' tmp.(8);
  tmp

let () =
  parse_input input |> List.iter (make_move piles);
  Array.iter
    (fun s -> Stack.top s |> print_char)
    piles
