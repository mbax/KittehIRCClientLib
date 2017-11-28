/*
 * * Copyright (C) 2013-2017 Matt Baxter http://kitteh.org
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kitteh.irc.client.library.event.client;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.util.ToStringer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The {@link Client} has had a working connection cease.
 */
public class ClientConnectionClosedEvent extends ClientConnectionEndedEvent {
    @Nullable
    private final String lastMessage;

    /**
     * Constructs the event.
     *
     * @param client client for which this is occurring
     * @param reconnecting true if the client plans to reconnect
     * @param exception exception, if there was one, closing it
     * @param lastMessage last message received
     */
    public ClientConnectionClosedEvent(@Nonnull Client client, boolean reconnecting, @Nullable Exception exception, @Nullable String lastMessage) {
        super(client, reconnecting, exception);
        this.lastMessage = lastMessage;
    }

    /**
     * Gets the last message sent prior to disconnect. Useful if killed from
     * the server. This message may not have been processed yet by other
     * events.
     *
     * @return last message, or empty, sent by the server
     */
    @Nonnull
    public Optional<String> getLastMessage() {
        return Optional.ofNullable(this.lastMessage);
    }

    @Override
    @Nonnull
    protected ToStringer toStringer() {
        return super.toStringer().add("lastMessage", this.lastMessage);
    }
}
