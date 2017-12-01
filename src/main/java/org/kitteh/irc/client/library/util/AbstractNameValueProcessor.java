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
package org.kitteh.irc.client.library.util;

import org.kitteh.irc.client.library.Client;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract class for registering and processing name/value pairs.
 *
 * @param <NameValue> type of pair
 */
public abstract class AbstractNameValueProcessor<NameValue> {
    /**
     * A creator of name/value pairs of a particular type.
     *
     * @param <NameValue> type of pair
     */
    public static class Creator<NameValue> {
        private final TriFunction<Client, String, String, ? extends NameValue> function;

        /**
         * Constructs the creator.
         *
         * @param function function to do the work
         */
        public Creator(@Nonnull TriFunction<Client, String, String, ? extends NameValue> function) {
            this.function = Sanity.nullCheck(function, "Function cannot be null");
        }

        /**
         * Gets the creator's function.
         *
         * @return function
         */
        @Nonnull
        public TriFunction<Client, String, String, ? extends NameValue> getFunction() {
            return this.function;
        }
    }

    private final Client.WithManagement client;
    private final Map<String, Creator<NameValue>> registeredNames = new ConcurrentHashMap<>();

    protected AbstractNameValueProcessor(Client.WithManagement client) {
        this.client = client;
    }

    /**
     * Gets the Client for which this processor functions.
     *
     * @return client
     */
    public Client.WithManagement getClient() {
        return this.client;
    }

    protected final Map<String, Creator<NameValue>> getRegistrations() {
        return this.registeredNames;
    }

    @Nonnull
    protected final Optional<TriFunction<Client, String, String, ? extends NameValue>> getCreatorByName(@Nonnull String name) {
        return this.optional(this.registeredNames.get(Sanity.nullCheck(name, "Name cannot be null")));
    }

    @Nonnull
    protected final Optional<TriFunction<Client, String, String, ? extends NameValue>> registerCreator(@Nonnull String name, @Nonnull Creator<NameValue> creator) {
        return this.optional(this.registeredNames.put(Sanity.nullCheck(name, "Name cannot be null"), creator));
    }

    @Nonnull
    protected final Optional<TriFunction<Client, String, String, ? extends NameValue>> unregisterCreator(@Nonnull String name) {
        return this.optional(this.registeredNames.remove(Sanity.nullCheck(name, "Name cannot be null")));
    }

    @Nonnull
    private Optional<TriFunction<Client, String, String, ? extends NameValue>> optional(@Nullable Creator<NameValue> creator) {
        return (creator == null) ? Optional.empty() : Optional.of(creator.getFunction());
    }

    @Nonnull
    @Override
    public String toString() {
        return new ToStringer(this).toString();
    }
}