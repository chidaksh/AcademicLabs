	.data
n:
	12
	.text
main:
	load %x0, $n, %x6
	addi %x0, 1, %x3
	addi %x0, 2, %x8
	addi %x0, 65535, %x9
	addi %x0, 0, %x5
	addi %x3, 0, %x4
	store %x0, 0, %x9
	subi %x9, 1, %x9
intialize:
	store %x3, 0, %x9
	subi %x9, 1, %x9
	addi %x5, 1, %x5
	beq %x5, %x8, febonacci
	jmp intialize
febonacci:
	add %x3, %x4, %x10
	addi %x4, 0, %x3
	addi %x10, 0, %x4
	store %x4, 0, %x9
	subi %x9, 1, %x9
	addi %x5, 1, %x5
	beq %x5, %x6, done
	jmp febonacci
done:
	end