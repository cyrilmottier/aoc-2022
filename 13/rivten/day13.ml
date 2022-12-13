let chunked n l =
  let aux (res, acc) e =
    if List.length acc == n then (acc :: res, [ e ]) else (res, e :: acc)
  in
  List.fold_left aux ([], []) l |> fst |> List.map List.rev

type packet = PacketList of packet list | PacketInt of int

let advance s n = String.sub s n (String.length s - n)

let parse_packet_int s =
  let len =
    min
      (String.index_from_opt s 0 ']' |> Option.value ~default:(String.length s))
      (String.index_from_opt s 0 ',' |> Option.value ~default:(String.length s))
  in
  (int_of_string (String.sub s 0 len), advance s len)

let rec parse_packet_list (s : string) : packet list * string =
  if s.[0] == ']' then ([], advance s 1)
  else
    let p, s = parse_packet s in
    let rm, s = parse_packet_list s in
    (p :: rm, s)

and parse_packet (s : string) : packet * string =
  if s.[0] == '[' then
    let p, i = parse_packet_list (advance s 1) in
    (PacketList p, i)
  else if s.[0] == ',' then parse_packet (advance s 1)
  else
    let p, s = parse_packet_int s in
    (PacketInt p, s)

let input =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.trim |> String.split_on_char '\n' |> chunked 3
  |> List.map (fun [ a; b; _ ] -> [ a; b ])
  |> List.rev
  |> List.map
       (List.map (fun s ->
            let p, _ = parse_packet s in
            p))

let rec compare_packet pa pb =
  match (pa, pb) with
  | PacketInt a, PacketInt b -> compare a b
  | PacketList [], PacketList [] -> 0
  | PacketList _, PacketList [] -> 1
  | PacketList [], PacketList _ -> -1
  | PacketList (a :: ra), PacketList (b :: rb) ->
      let c = compare_packet a b in
      if c == 0 then compare_packet (PacketList ra) (PacketList rb) else c
  | PacketInt a, PacketList _ -> compare_packet (PacketList [ PacketInt a ]) pb
  | PacketList _, PacketInt b -> compare_packet pa (PacketList [ PacketInt b ])

let part1 =
  input
  |> List.mapi (fun i [ pa; pb ] -> (i + 1, compare_packet pa pb == -1))
  |> List.filter_map (fun (i, b) -> if b then Some i else None)
  |> List.fold_left ( + ) 0
