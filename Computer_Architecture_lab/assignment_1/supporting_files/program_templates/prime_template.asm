	.data
a:
	10
	.text
main:
	add %x0, %x0, %x3
	load %x0, $a, %x4
	addi %x3, 2, %x3
loop:
	beq %x4, %x3, isprime
	div %x4, %x3, %x5
	mul %x5, %x3, %x6
	beq %x4, %x6, notprime
	addi %x3, 1, %x3
	beq %x4, %x3, isprime
	jmp loop
isprime:
	addi %x0, 1, %x10
	end
notprime:
	subi %x0, 1, %x10
	end