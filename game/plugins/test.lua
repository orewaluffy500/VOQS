Registry.Register("plank", {255, 100, 140, 255}, "block/plank.png")

BlockId = "dirt"

function BlockPulse(x, y)
    if Clock.GetPulseIndex() == 6 then
        local px, py = Player.Position()

        if py == y and px == x + 1 then
            Terrain.PlaceBlock(x + 4, y, Player.SelectedBlock())
        end
    end
end