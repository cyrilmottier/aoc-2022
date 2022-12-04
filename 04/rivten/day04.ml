let input_parsed =
  In_channel.with_open_bin "input.txt" In_channel.input_all
  |> String.trim |> String.split_on_char '\n'
  |> List.map (fun s ->
         String.split_on_char ',' s
         |> List.map (String.split_on_char '-')
         |> List.map (List.map int_of_string)
         |> fun [ [ a; b ]; [ c; d ] ] ->
         if a <= c then ((a, b), (c, d)) else ((c, d), (a, b)))

let day01 =
  input_parsed
  |> List.filter (fun ((a, b), (c, d)) -> a == c || d <= b)
  |> List.length

let day02 =
  input_parsed
  |> List.filter (fun ((a, b), (c, d)) -> a == c || c <= b)
  |> List.length
