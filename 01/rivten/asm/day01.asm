section .bss
buffer: resb 0x100
.len equ $ - buffer

section .rodata
fmt: db "result: %i", 10, 0

section .text
extern fgets
extern stdin
extern printf
extern strlen
extern atoi

global main

main:
	push rbp
	mov rbp, rsp

	mov rbx, 0 ; rbx contains the result: the maximum found
	mov r12, 0  ; r12 contains the sum of the weights for the current elf

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
	cmp r12, rbx
	jl .reset_elf
	; found heavier elf
	mov rbx, r12

.reset_elf:
	mov r12, 0
	jmp .start_loop

.end_loop:
	; we still need to compare values for the last elf
	cmp r12, rbx
	jl .end
	mov rbx, r12

.end:
	; display the result on the screen
	lea rdi, [rel fmt]
	mov rsi, rbx
	call printf wrt ..plt

	mov rax, 0
	pop rbp
	ret
