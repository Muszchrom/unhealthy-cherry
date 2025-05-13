"use client"

import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";

import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { toast } from "sonner";
import { useRouter } from "next/navigation";
import { signIn } from "next-auth/react";

const formSchema = z.object({
  username: z.string().min(3, {
    message: "Username must be at least 3 characters."
  }).max(24, {
    message: "Username must be shoter than 25 characters."
  }),
  password: z.string().min(6, {
    message: "Password must be at least 6 characters."
  }).max(255, {
    message: "Password must be shoter than 256 characters."
  })
})

export function LoginForm() {
  const router = useRouter();

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      password: ""
    }
  })

  async function onSubmit(values: z.infer<typeof formSchema>) {
    const res = await signIn("credentials", {
      username: values.username,
      password: values.password,
      redirect: false
    })

    if (!res?.error) {
      router.push("/");
      toast.success("Jesteś teraz zalogowany/a!");
    } else {
      toast.error("Logowanie nie powiodło się", {
        description: "Niepoprawna nazwa użytkownika i/lub hasło"
      })
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="username"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Login</FormLabel>
              <FormControl>
                <Input placeholder="Scaresca20" {...field} />
              </FormControl>
              <FormDescription>
                Pole na wprowadzenie loginu.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
          />

        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Hasło</FormLabel>
              <FormControl>
                <Input type="password" placeholder="Super silne hasło" {...field} />
              </FormControl>
              <FormDescription>
                Pole na wprowadzenie hasła.
              </FormDescription>
              <FormMessage />
            </FormItem>
          )}
          />
        <div className="flex justify-end">
          <Button type="submit">Dalej</Button>
        </div>
      </form>
    </Form>
  )
}
