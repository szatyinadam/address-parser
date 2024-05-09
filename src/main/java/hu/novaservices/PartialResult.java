package hu.novaservices;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
class PartialResult {
    private final int left;
    private final int right;
    private final int contentLeft;
    private final int contentRight;
}
