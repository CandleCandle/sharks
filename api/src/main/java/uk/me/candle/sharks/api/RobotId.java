package uk.me.candle.sharks.api;

import java.util.Objects;

public class RobotId {

    private final String name;

    public RobotId(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final RobotId other = (RobotId) obj;
        return Objects.equals(other.name, this.name);
    }

    @Override
    public String toString() {
        return "RobotId{" + "name=" + name + "} [" + super.toString() + "]";
    }


}
