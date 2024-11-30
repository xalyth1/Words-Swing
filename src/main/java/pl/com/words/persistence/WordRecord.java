package pl.com.words.persistence;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public record WordRecord(String headword,
                         Set<String> definitions) {
    //@Override
    //public boolean equals(Object o) {
    //    if (this == o) return true;
    //    if (!(o instanceof WordRecord other)) return false;
    //    return Objects.equals(this.headword, other.headword) &&
    //            this.definitions.size() == other.definitions.size() &&
    //            this.definitions.containsAll(other.definitions);
    //}
}
