	.data
a:
	10
	.text
main:
	add %x0, %x0, %x10
	load %x0, 0, %x3
	add %x3, %x10, %x4
	add %x10, %x10, %x5
loop:
	divi %x4, 10, %x4
	add %x5, %x31, %x5
	beq %x4, %x0, break
	muli %x5, 10, %x5
	jmp loop
break:
	beq %x5, %x3, done
	subi %x10, 1, %x10
	end
done:
	addi %x10, 1, %x10
	end