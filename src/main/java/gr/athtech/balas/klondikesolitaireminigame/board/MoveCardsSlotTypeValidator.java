package gr.athtech.balas.klondikesolitaireminigame.board;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class MoveCardsSlotTypeValidator {
    private final Map<SlotType, Set<SlotType>> validMovesMap;

    public MoveCardsSlotTypeValidator() {
        validMovesMap = new EnumMap<>(SlotType.class);//enumMap requires enumType..

        validMovesMap.put(SlotType.DECK, EnumSet.of(SlotType.WASTE));
        validMovesMap.put(SlotType.WASTE, EnumSet.of(SlotType.DECK));
        validMovesMap.put(SlotType.FOUNDATION, EnumSet.of(SlotType.WASTE, SlotType.TABLE));
        validMovesMap.put(SlotType.TABLE, EnumSet.of(SlotType.WASTE, SlotType.TABLE, SlotType.FOUNDATION));
    }

    public boolean isMoveToCardSlotValid(BoardCardsSlot from, BoardCardsSlot to) {
        if (from == null || to == null) {
            return false;
        }
        Set<SlotType> validFromTypes = validMovesMap.get(to.getSlotType());
        return validFromTypes != null && validFromTypes.contains(from.getSlotType());
    }
}