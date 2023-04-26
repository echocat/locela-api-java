package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public abstract class MessagesSupport implements Messages {

    @Nonnull
    @Override
    public Message get(@Nonnull String id) {
        final Message message = find(id);
        return message != null ? message : dummyMessageFor(id);
    }

    @Nonnull
    protected DummyMessage dummyMessageFor(@Nonnull String id) {
        return new DummyMessage(null, id);
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else  if (!(o instanceof Messages)) {
            result = false;
        } else {
            final Messages that = (Messages) o;
            final Set<String> thisIds = getIdsOf(this);
            final Set<String> thatIds = getIdsOf(that);
            if (thisIds.equals(thatIds)) {
                result = true;
                for (final Message thisMessage : this) {
                    final Message thatMessage = that.get(thisMessage.getId());
                    if (!thisMessage.equals(thatMessage)) {
                        result = false;
                        break;
                    }
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    @Nonnull
    protected Set<String> getIdsOf(@Nonnull Iterable<Message> messages) {
        final Set<String> result = new HashSet<>();
        for (final Message message : messages) {
            result.add(message.getId());
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (final String id : getIdsOf(this)) {
            result = 31 * result + get(id).hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean firstMessage = true;
        for (final Message message : this) {
            if (firstMessage) {
                sb.append('\n');
                firstMessage = false;
            }
            sb.append("    ").append(message).append('\n');
        }
        sb.append('}');
        return sb.toString();
    }

}
