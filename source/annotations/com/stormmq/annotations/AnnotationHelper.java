// The MIT License (MIT)
//
// Copyright © 2016, Raphael Cohn <raphael.cohn@stormmq.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.stormmq.annotations;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.*;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.util.EnumSet.allOf;
import static java.util.EnumSet.noneOf;

public final class AnnotationHelper
{
	@NotNull private static final Set<ElementType> DefaultTargets = defaultTargets();

	@NotNull
	private static Set<ElementType> defaultTargets()
	{
		final Set<ElementType> defaultTargets = allOf(ElementType.class);
		defaultTargets.remove(TYPE_PARAMETER);
		return defaultTargets;
	}

	private AnnotationHelper()
	{
	}

	public static boolean isRepeatable(@NotNull final Class<? extends Annotation> annotationClass)
	{
		return isAnnotationDirectlyPresentIgnoringInheritance(annotationClass, Repeatable.class);
	}

	public static boolean isInherited(@NotNull final Class<? extends Annotation> annotationClass)
	{
		return isAnnotationDirectlyPresentIgnoringInheritance(annotationClass, Inherited.class);
	}

	public static boolean isDocumented(@NotNull final Class<? extends Annotation> annotationClass)
	{
		return isAnnotationDirectlyPresentIgnoringInheritance(annotationClass, Documented.class);
	}

	public static boolean isAnnotationDirectlyPresentIgnoringInheritance(@NotNull final Class<? extends Annotation> annotationClass, @NotNull final Class<? extends Annotation> annotationClassOnAnnotationClass)
	{
		return annotationClass.getDeclaredAnnotation(annotationClassOnAnnotationClass) != null;
	}

	public static boolean doesAnnotationTargetPackages(@NotNull final Class<? extends Annotation> annotationClass)
	{
		return targets(annotationClass).contains(PACKAGE);
	}

	@NotNull
	public static Set<ElementType> targets(@NotNull final Class<? extends Annotation> annotationClass)
	{
		return useAnnotationDirectlyPresentIgnoringInheritance(annotationClass, Target.class, DefaultTargets, target ->
		{
			final ElementType[] value = target.value();
			final Set<ElementType> elementTypes = noneOf(ElementType.class);
			final int length = value.length;
			//noinspection ForLoopReplaceableByForEach,ManualArrayToCollectionCopy
			for (int index = 0; index < length; index++)
			{
				// Duplicates are permissible but not great
				elementTypes.add(value[index]);
			}
			return elementTypes;
		});
	}

	@NotNull
	public static RetentionPolicy retention(@NotNull final Class<? extends Annotation> annotationClass)
	{
		return useAnnotationDirectlyPresentIgnoringInheritance(annotationClass, Retention.class, RetentionPolicy.CLASS, Retention::value);
	}

	@NotNull
	public static <A extends Annotation, R> R useAnnotationEvenIfInherited(@NotNull final Class<? extends Annotation> annotationClass, @NotNull final Class<A> key, @NotNull final R ifAbsent, @NotNull final Function<A, R> ifPresent)
	{
		@Nullable final A annotation = annotationClass.getAnnotation(key);
		if (annotation == null)
		{
			return ifAbsent;
		}
		return ifPresent.apply(annotation);
	}

	@NotNull
	public static <A extends Annotation, R> R useAnnotationEvenIfInherited(@NotNull final Class<? extends Annotation> annotationClass, @NotNull final Class<A> key, @NotNull final Supplier<R> ifAbsent, @NotNull final Function<A, R> ifPresent)
	{
		@Nullable final A annotation = annotationClass.getAnnotation(key);
		if (annotation == null)
		{
			return ifAbsent.get();
		}
		return ifPresent.apply(annotation);
	}

	@NotNull
	public static <A extends Annotation, R> R useAnnotationDirectlyPresentIgnoringInheritance(@NotNull final Class<? extends Annotation> annotationClass, @NotNull final Class<A> key, @NotNull final R ifAbsent, @NotNull final Function<A, R> ifPresent)
	{
		@Nullable final A annotation = annotationClass.getDeclaredAnnotation(key);
		if (annotation == null)
		{
			return ifAbsent;
		}
		return ifPresent.apply(annotation);
	}

	@NotNull
	public static <A extends Annotation, R> R useAnnotationDirectlyPresentIgnoringInheritance(@NotNull final Class<? extends Annotation> annotationClass, @NotNull final Class<A> key, @NotNull final Supplier<R> ifAbsent, @NotNull final Function<A, R> ifPresent)
	{
		@Nullable final A annotation = annotationClass.getDeclaredAnnotation(key);
		if (annotation == null)
		{
			return ifAbsent.get();
		}
		return ifPresent.apply(annotation);
	}
}
