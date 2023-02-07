package com.jessebrault.gcp.token;

import java.util.function.Function;

interface FsmFunction extends Function<CharSequence, FsmOutput> {}
