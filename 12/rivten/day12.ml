let input =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.trim |> String.split_on_char '\n'
  |> List.map (fun s -> String.fold_left (fun l a -> Char.code a :: l) [] s)
  |> List.map List.rev |> List.map Array.of_list |> Array.of_list

let height = Array.length input
let width = Array.length input.(0)

(* taken and slightly updated from https://github.com/nilehmann/ocaml-algorithms/blob/master/dijkstra.ml *)
(* ------- *)
module P = struct
  type t = int * int

  let compare = compare
end

module PairSet = Set.Make (P)

let dijkstra (g : int list array) s e =
  let n = Array.length g in
  let dist = Array.make n max_int in
  let q = ref PairSet.(empty |> add (0, s)) in
  dist.(s) <- 0;
  while not (PairSet.is_empty !q) do
    let d, u = PairSet.min_elt !q in
    q := PairSet.remove (d, u) !q;
    List.iter
      (fun v ->
        let newdist = dist.(u) + 1 in
        if newdist < dist.(v) then (
          q := PairSet.add (newdist, v) !q;
          dist.(v) <- newdist))
      g.(u)
  done;
  dist.(e)
(* ------- *)

let graph_index i j width = (i * width) + j

let s, e =
  let s = ref 0 in
  let e = ref 0 in
  for i = 0 to height - 1 do
    for j = 0 to width - 1 do
      if input.(i).(j) == 83 then (
        s := graph_index i j width;
        input.(i).(j) <- 97)
      else if input.(i).(j) == 69 then (
        e := graph_index i j width;
        input.(i).(j) <- 122)
    done
  done;
  (!s, !e)

let g =
  let g = Array.make (width * height) [] in
  for i = 0 to height - 1 do
    for j = 0 to width - 1 do
      let v = input.(i).(j) and v_index = graph_index i j width in
      if i > 0 && input.(i - 1).(j) - v <= 1 then
        g.(v_index) <- graph_index (i - 1) j width :: g.(v_index);
      if i < height - 1 && input.(i + 1).(j) - v <= 1 then
        g.(v_index) <- graph_index (i + 1) j width :: g.(v_index);
      if j > 0 && input.(i).(j - 1) - v <= 1 then
        g.(v_index) <- graph_index i (j - 1) width :: g.(v_index);
      if j < width - 1 && input.(i).(j + 1) - v <= 1 then
        g.(v_index) <- graph_index i (j + 1) width :: g.(v_index)
    done
  done;
  g

let part1 = Printf.printf "%i\n" (dijkstra g s e)

let part2 =
    let poses = 
        let p = ref [] in
        for i = 0 to height - 1 do
            for j = 0 to width - 1 do
                if input.(i).(j) == 97 then p := (graph_index i j width)::!p
            done
        done;
        !p
    in
    List.fold_left min (dijkstra g s e) (List.map (fun a -> dijkstra g a e) poses)
