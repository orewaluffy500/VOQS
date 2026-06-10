
BlockId = "dirt"

function Placed(x, y)
    if x == 3 and y == 3 then
        Player.TeleportTo(15, 15)
    end
end