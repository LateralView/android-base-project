/*
 * Copyright © 2017 Razer Inc. All rights reserved.
 */

package co.lateralview.myapp.ui.util.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScoped
{
}
