	.data
a:	
	70
	20
	100
	4
	35
	200
	45
	1
	110
n:
	9
	.text
main:
	load %x0, $n, %x3
	addi %x0, 1, %x11
	beq %x3, %x11, check
	subi %x3, 1, %x4
	add %x0, %x4, %x5
	addi %x0, 0, %x6
sort:
	addi %x6, 1, %x7
	beq %x5, %x11, done
insidecheck:
	beq %x6, %x5, reintialise
	jmp proceed
reintialise:
	subi %x5, 1, %x5
	addi %x0, 0, %x6
	addi %x6, 1, %x7
proceed:
	load %x6, $a, %x8
	load %x7, $a, %x9
	bgt %x9, %x8, rearrange
update:
	addi %x6, 1, %x6
	jmp sort
rearrange:
	store %x9, 0, %x6
	store %x8, 0, %x7
	jmp update
check:
	end
done:
	end