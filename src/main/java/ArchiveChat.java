import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
class ArchiveChat {
    private List<String> archive = new ArrayList<>();

    void add(String text) {
        archive.add(text);
    }
}
