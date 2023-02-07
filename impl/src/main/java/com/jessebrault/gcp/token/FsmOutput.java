package com.jessebrault.gcp.token;

interface FsmOutput {
    CharSequence entire();
    CharSequence part(int index);
}
