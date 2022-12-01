section .bss
buffer: resb 0x100
.len equ $ - buffer

section .rodata
fmt1: db "result part 1: %i", 10, 0
fmt2: db "result part 2: %i", 10, 0

section .text
extern fgets
extern stdin
extern printf
extern strlen
extern atoi

global main

adjust_elf:
	cmp r12, rbx
	jl .compare_second
	; found heavier elf
	mov r14, r13 ; the second becomes the third
	mov r13, rbx ; the first becomes second
	mov rbx, r12 ; the new one becomes the first
	jmp .reset_elf

.compare_second:
	cmp r12, r13
	jl .compare_third
	; found heavier elf
	mov r14, r13
	mov r13, r12
	jmp .reset_elf

.compare_third:
	cmp r12, r14
	jl .reset_elf
	; found heavier elf
	mov r14, r12

.reset_elf:
	mov r12, 0
	ret

main:
	push rbp
	mov rbp, rsp

	mov rbx, 0 ; rbx contains the result: the maximum found
	mov r12, 0 ; r12 contains the sum of the weights for the current elf

	mov r13, 0 ; r13 contains the weight of the 2nd heavier elf
	mov r14, 0 ; r14 contains the weight of the 3rd heavier elf

.start_loop:
	lea rdi, [rel buffer]
	mov esi, buffer.len
	mov rdx, [rel stdin]
	call fgets wrt ..plt
	cmp rax, 0
	je .end_loop

	lea rdi, [rel buffer]
	call strlen wrt ..plt

	cmp rax, 1 ; if the len is one, it means that the line is "\n"
	je .end_elf

	lea rdi, [rel buffer]
	call atoi wrt ..plt
	add r12, rax

	jmp .start_loop

.end_elf:
	call adjust_elf
	jmp .start_loop

.end_loop:
	call adjust_elf

	; display the result on the screen
	lea rdi, [rel fmt1]
	mov rsi, rbx
	call printf wrt ..plt

	lea rdi, [rel fmt2]
	mov rsi, rbx
	add rsi, r13
	add rsi, r14
	call printf wrt ..plt

	mov rax, 0
	pop rbp
	ret
