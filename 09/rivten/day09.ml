let moves =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.trim |> String.split_on_char '\n'
  |> List.map (String.split_on_char ' ')
  |> List.map (fun [ a; b ] -> (a, int_of_string b))

module Pos = struct
  type t = int * int

  let compare = compare
end

module S = Set.Make (Pos)

let do_head_move dir (hx, hy) =
  match dir with
  | "R" -> (hx + 1, hy)
  | "L" -> (hx - 1, hy)
  | "U" -> (hx, hy + 1)
  | "D" -> (hx, hy - 1)
  | _ -> failwith "do_head_move error"

let sign x = if x == 0 then 0 else if x < 0 then -1 else 1
let offset d = sign d * min 1 (Int.abs d)

let do_tail_move (hx, hy) (tx, ty) =
  let dx, dy = (hx - tx, hy - ty) in
  if max (Int.abs dx) (Int.abs dy) == 1 then (tx, ty)
  else (tx + offset dx, ty + offset dy)

let do_single_move dir h t =
  let h = do_head_move dir h in
  let t = do_tail_move h t in
  (h, t)

let do_single_move_2 dir h t =
  let h = do_head_move dir h in
  t.(0) <- do_tail_move h t.(0);
  for i = 1 to 8 do
    t.(i) <- do_tail_move t.(i - 1) t.(i)
  done;
  (h, Array.copy t)

let rec do_move dir count h t =
  if count == 0 then [ (h, t) ]
  else
    let h2, t2 = do_single_move dir h t in
    (h, t) :: do_move dir (count - 1) h2 t2

let rec do_move_2 dir count h t =
  if count == 0 then [ (h, t) ]
  else
    let h2, t2 = do_single_move_2 dir h t in
    (h, t) :: do_move_2 dir (count - 1) h2 t2

let move_reduce (h, t, tposes) (dir, count) =
  let moves = do_move dir count h t in
  let h, t = List.hd (List.rev moves) in
  let tposes =
    List.fold_left
      (fun s e -> S.add e s)
      tposes
      (List.map (fun (_, b) -> b) moves)
  in
  (h, t, tposes)

let move_reduce_2 (h, t, tposes) (dir, count) =
  let moves = do_move_2 dir count h t in
  let h, t = List.hd (List.rev moves) in
  let tposes =
    List.fold_left
      (fun s e -> S.add e s)
      tposes
      (List.map (fun (_, tail) -> tail.(8)) moves)
  in
  (h, t, tposes)

let day01 =
  let _, _, tposes =
    List.fold_left move_reduce ((0, 0), (0, 0), S.empty) moves
  in
  S.cardinal tposes

let day02 =
  let _, _, tposes =
    List.fold_left move_reduce_2 ((0, 0), Array.make 9 (0, 0), S.empty) moves
  in
  S.cardinal tposes
