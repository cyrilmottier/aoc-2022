type monkey = {
  starting_items : int list;
  stress_op : int -> int;
  test_func : int -> bool;
  monkey_true : int;
  monkey_false : int;
}

let get_starting_items line =
  List.nth (String.split_on_char ':' line) 1
  |> String.trim |> String.split_on_char ',' |> List.map String.trim
  |> List.map int_of_string

let get_stress_op line =
  let other_side =
    line |> String.split_on_char ':' |> fun [ _; op_string ] ->
    op_string |> String.split_on_char '=' |> fun [ _; op_string ] ->
    op_string |> String.trim
  in
  if String.contains other_side '+' then
    let [ _; other ] =
      String.split_on_char '+' other_side |> List.map String.trim
    in
    if String.equal other "old" then fun x -> 2 * x
    else
      let o = int_of_string other in
      fun x -> x + o
  else
    let [ _; other ] =
      String.split_on_char '*' other_side |> List.map String.trim
    in
    if String.equal other "old" then fun x -> x * x
    else
      let o = int_of_string other in
      fun x -> x * o

let rec last l =
  match l with
  | [] -> failwith "last: empty list"
  | [ x ] -> x
  | h :: tl -> last tl

let get_test_func line =
  let div_by = line |> String.split_on_char ' ' |> last |> int_of_string in
  fun x -> x mod div_by == 0

let get_monkey_index line =
  line |> String.split_on_char ' ' |> last |> int_of_string

let parse_monkey monkey_id_line starting_item_line operation_line test_line
    monkey_true_line monkey_false_line =
  {
    starting_items = get_starting_items starting_item_line;
    stress_op = get_stress_op operation_line;
    test_func = get_test_func test_line;
    monkey_true = get_monkey_index monkey_true_line;
    monkey_false = get_monkey_index monkey_false_line;
  }

let rec parse_monkeys input =
  match input with
  | [] -> []
  | a :: b :: c :: d :: e :: f :: _ :: rm ->
      parse_monkey a b c d e f :: parse_monkeys rm
  | [ a; b; c; d; e; f ] -> [ parse_monkey a b c d e f ]
  | _ -> failwith "parse_monkeys error"

let input =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.trim |> String.split_on_char '\n' |> parse_monkeys

let print_monkey_items monkey_items =
  Hashtbl.iter
    (fun monkey_index q ->
      Printf.printf "Monkey %i :" monkey_index;
      Queue.iter (fun item -> Printf.printf " %i " item) q;
      print_newline ())
    monkey_items

let do_round input monkey_items monkey_index =
  match Hashtbl.find monkey_items monkey_index |> Queue.take_opt with
  | None -> ()
  | Some worry_level ->
      let monkey_data = Hashtbl.find input monkey_index in
      let next_worry_level = monkey_data.stress_op worry_level / 3 in
      if monkey_data.test_func next_worry_level then
        Hashtbl.find monkey_items monkey_data.monkey_true
        |> Queue.push next_worry_level
      else
        Hashtbl.find monkey_items monkey_data.monkey_false
        |> Queue.push next_worry_level

let part1 =
  let input_data =
    let h = Hashtbl.create 8 in
    List.iteri (fun i monkey_data -> Hashtbl.add h i monkey_data) input;
    h
  in
  let monkey_items =
    let h = Hashtbl.create 8 in
    List.iteri
      (fun i monkey_data ->
        let q = Queue.create () in
        List.iter (fun item -> Queue.add item q) monkey_data.starting_items;
        Hashtbl.add h i q)
      input;
    h
  in
  let inspect_count_table =
    let h = Hashtbl.create 8 in
    List.iteri (fun i monkey_data -> Hashtbl.add h i 0) input;
    h
  in
  let monkey_count = Hashtbl.length monkey_items in
  for round_index = 1 to 20 do
    for monkey_index = 0 to monkey_count - 1 do
      while not (Queue.is_empty (Hashtbl.find monkey_items monkey_index)) do
        Hashtbl.replace inspect_count_table monkey_index
          (1 + Hashtbl.find inspect_count_table monkey_index);
        do_round input_data monkey_items monkey_index;
        Printf.printf "----\nRound %i - Monkey %i\n----\n" round_index
          monkey_index;
        print_monkey_items monkey_items
      done
    done
  done;
  Hashtbl.iter
    (fun monkey_index inspect_count ->
      Printf.printf "Monkey %i inspected %i items\n" monkey_index inspect_count)
    inspect_count_table;
  let first, second =
    Hashtbl.fold
      (fun _ inspect_count (f, s) ->
        if inspect_count > f then (inspect_count, f)
        else if inspect_count > s then (f, inspect_count)
        else (f, s))
      inspect_count_table (0, 0)
  in
  first * second
