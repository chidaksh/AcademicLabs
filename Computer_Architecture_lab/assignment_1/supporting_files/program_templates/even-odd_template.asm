	.data
a:
	10
	.text
main:
	load %x0, $a, %x4
	divi %x4, 2, %x5
	muli %x5, 2, %x6
	beq %x4, %x6, check
	addi %x0, 1, %x10
	end
check:
	subi %x0, 1, %x10
	end
