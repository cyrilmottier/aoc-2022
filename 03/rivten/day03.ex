
defmodule Day03 do
  defp get_double_items_overall_and_length([], _, doubles, len) do
    {doubles, len}
  end

  defp get_double_items_overall_and_length([head | tail], singles, doubles, len) do
    case singles[head] do
      nil -> get_double_items_overall_and_length(tail, Map.put(singles, head, len), doubles, len + 1)
      pos -> get_double_items_overall_and_length(tail, singles, [{head, pos, len} | doubles], len + 1)
    end
  end

  defp get_item_score(item) do
    <<item_value::utf8>> = item
    cond do
      item_value >= 97 && item_value < 123 -> item_value - 96
      item_value >= 65 && item_value < 91 -> item_value - 64 + 26
      true -> raise "error"
    end
  end

  defp find_double_item(items) do
    {doubles, len} = get_double_items_overall_and_length(String.graphemes(items), %{}, [], 0)
    Enum.filter(doubles, fn({_, first_pos, other_pos}) -> first_pos * 2 < len && other_pos * 2 >= len end)
    |> Enum.map(fn({item, _, _}) -> item end)
    |> hd
    |> get_item_score
  end

  def result() do
    IO.read(:stdio, :eof)
    |> String.split("\n", trim: true)
    |> Enum.map(&find_double_item/1)
    |> Enum.sum
    |> IO.inspect
  end

  def find_badge([a, b, c]) do
    Enum.filter(a, fn item -> item in b and item in c end) |> hd
  end

  def result2() do
    IO.read(:stdio, :eof)
    |> String.split("\n", trim: true)
    |> Enum.chunk_every(3)
    |> Enum.map(fn l -> Enum.map(l, &String.graphemes/1) end)
    |> Enum.map(&find_badge/1)
    |> Enum.map(&get_item_score/1)
    |> Enum.sum
    |> IO.inspect
  end
end

