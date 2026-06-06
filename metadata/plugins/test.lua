
function Pulse()
    local selected = Player.GetSelectedBlock()

    if selected == "stone" and Keys.Held("F") then
        local x, y = Player.GetPosition()

        Player.TeleportTo(x + 5, y)
        Player.SelectBlock("dirt")
        Metadata.PlayerMeta("stone_tp.used").Value = true
    end
end

function Exit()
    local used = Metadata.PlayerMeta("stone_tp.used").Value

    if used then
        Logger.Notify("Used")
    else
        Logger.Warn("Warn", "Not used")
    end
end