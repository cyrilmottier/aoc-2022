let split_on s l =
    let rec aux acc curr l = match l with
    | [] -> acc
    | e::r -> if String.equal s e 
                then aux (curr::acc) [] r
                else aux acc (e::curr) r

    in aux [] [] l

let rec max_list l = match l with
    | [e] -> e
    | e::r -> max e (max_list r)
    | [] -> 0

let result channel = channel
    |> In_channel.input_all 
    |> String.split_on_char '\n' 
    |> split_on "" 
    |> List.map (List.map int_of_string) 
    |> List.map (List.fold_left (+) 0)
    |> List.sort (compare)
    |> List.rev
    |> fun l -> match l with | a::b::c::r -> (a, a + b + c) | _ -> failwith "error"

let () = let (p1, p2) = result In_channel.stdin
    in print_endline (string_of_int p1); print_endline (string_of_int p2)
