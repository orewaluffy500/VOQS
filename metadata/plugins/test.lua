
function Pulse()
    local selected = Player.GetSelectedBlock()

    if selected == "stone" and Keys.Held("F") then
        local x, y = Player.GetPosition()

        Player.TeleportTo(x + 5, y)
    end
end